package com.ibm.spp.services;

import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.ibm.spp.domain.RegistrationInfo;
import com.ibm.spp.domain.SppSession;
import com.ibm.spp.domain.SppUrls;

public class SppServiceImpl implements SppService {

	private static CloseableHttpClient createAcceptSelfSignedCertificateClient()
            throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {

        SSLContext sslContext = SSLContextBuilder.create().loadTrustMaterial(new TrustSelfSignedStrategy()).build();
        HostnameVerifier allowAllHosts = new NoopHostnameVerifier();
        SSLConnectionSocketFactory connectionFactory = new SSLConnectionSocketFactory(sslContext, allowAllHosts);
        return HttpClients.custom().setSSLSocketFactory(connectionFactory).build();
    }
	
	@Override
	public String register(String registrationInfo) {
		RegistrationInfo regInfo = new Gson().fromJson(registrationInfo, RegistrationInfo.class);
		SppSession session = sppLogIn(regInfo.getSppHost(), regInfo.getSppUser(), regInfo.getSppPass());
		sppLogOut(regInfo.getSppHost(), session);
		return "Registration succesful";
	}

	public SppSession sppLogIn(String host, String user, String pass) {
		String sppSesUrl = host + SppUrls.sppSessionUrl;
		String userPassText = user + ":" + pass;
		String b64UserPass = new String(Base64.getEncoder().encode(userPassText.getBytes()));
		try {
			CloseableHttpClient httpclient = createAcceptSelfSignedCertificateClient();
			HttpUriRequest request = RequestBuilder.post().setUri(sppSesUrl).build();
			request.setHeader("Authorization", "Basic " + b64UserPass);
			HttpResponse response = httpclient.execute(request);
			HttpEntity entity = response.getEntity();
			String responseString = EntityUtils.toString(entity, "UTF-8");
			SppSession sppSession = new Gson().fromJson(responseString, SppSession.class);
			return sppSession;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new SppSession("Failure to create session");
	}
	
	public String sppLogOut(String host, SppSession session) {
		String sppSesUrl = host + SppUrls.sppSessionUrl;
		String sesIdHdr = session.getSessionId();
		try {
			CloseableHttpClient httpclient = createAcceptSelfSignedCertificateClient();
			HttpUriRequest request = RequestBuilder.delete().setUri(sppSesUrl).build();
			request.setHeader("X-Endeavour-Sessionid", sesIdHdr);
			HttpResponse response = httpclient.execute(request);
			if(response.getStatusLine().getStatusCode() == 204) {
				return "Log out succesful";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "Failed Logging Out";
	}

}
