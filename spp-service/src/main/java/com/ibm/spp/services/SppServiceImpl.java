package com.ibm.spp.services;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;

import org.apache.commons.lang3.SystemUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.ibm.spp.domain.RegistrationInfo;
import com.ibm.spp.domain.SppSession;
import com.ibm.spp.domain.SppUrls;

public class SppServiceImpl implements SppService {
	
	private static final Log log =
	         LogFactory.getLog(SppServiceImpl.class);

	private static CloseableHttpClient createAcceptSelfSignedCertificateClient()
			throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {

		SSLContext sslContext = SSLContextBuilder.create().loadTrustMaterial(new TrustSelfSignedStrategy()).build();
		HostnameVerifier allowAllHosts = new NoopHostnameVerifier();
		SSLConnectionSocketFactory connectionFactory = new SSLConnectionSocketFactory(sslContext, allowAllHosts);
		return HttpClients.custom().setSSLSocketFactory(connectionFactory).build();
	}
	
	// Main function to register SPP
	// Saving of registration is dependent on successful login
	// Currently returns String response of status
	@Override
	public String register(String registrationInfo) {
		RegistrationInfo regInfo = new Gson().fromJson(registrationInfo, RegistrationInfo.class);
		SppSession session = sppLogIn(regInfo.getSppHost(), regInfo.getSppUser(), regInfo.getSppPass());
		if(session.getSessionId() != null) {
			sppLogOut(regInfo.getSppHost(), session);
			setSppRegistrationInfo(registrationInfo);
			log.info("Registration complete");
			return "Registration succesful";
		} else {
			log.info("Registration failed");
			return "Registration failed";
		}
	}

	// Get JSON for all SLA policies in SPP
	@Override
	public String getSlaPolicies() {
		RegistrationInfo regInfo = getSppRegistrationInfo();
		String slaUrl = regInfo.getSppHost() + SppUrls.sppSlaUrl;
		SppSession session = sppLogIn(regInfo.getSppHost(), regInfo.getSppUser(), regInfo.getSppPass());
		try {
			CloseableHttpClient httpclient = createAcceptSelfSignedCertificateClient();
			HttpUriRequest request = RequestBuilder.get().setUri(slaUrl).build();
			request.setHeader("X-Endeavour-Sessionid", session.sessionid);
			HttpResponse response = httpclient.execute(request);
			HttpEntity entity = response.getEntity();
			String responseString = EntityUtils.toString(entity, "UTF-8");
			log.info("Returning SLA Policies from SPP");
			sppLogOut(regInfo.getSppHost(), session);
			return responseString;
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.error("Error getting SLA Policies from SPP");
		sppLogOut(regInfo.getSppHost(), session);
		return "Error getting SLA Policies";
	}
	
	// Get JSON for VM data from SPP
	// Uses the search API
	// This is a POST here but a GET in our controller
	@Override 
	public String getSppVmInfo(String vmName) {
		RegistrationInfo regInfo = getSppRegistrationInfo();
		String vmUrl = regInfo.getSppHost() + SppUrls.sppVmUrl;
		SppSession session = sppLogIn(regInfo.getSppHost(), regInfo.getSppUser(), regInfo.getSppPass());
		JsonObject searchJO = new JsonObject();
		searchJO.addProperty("name", vmName);
		searchJO.addProperty("hypervisorType", "vmware");
		try {
			CloseableHttpClient httpclient = createAcceptSelfSignedCertificateClient();
			HttpPost request = new HttpPost(vmUrl);
			request.setHeader("X-Endeavour-Sessionid", session.sessionid);
			request.setHeader("Accept" ,"application/json");
			request.setHeader("Content-Type" ,"application/json");
			StringEntity body = new StringEntity(searchJO.toString());
			request.setEntity(body);
			HttpResponse response = httpclient.execute(request);
			HttpEntity entity = response.getEntity();
			String responseString = EntityUtils.toString(entity, "UTF-8");
			log.info("Returning VM Info from SPP");
			sppLogOut(regInfo.getSppHost(), session);
			return responseString;
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.error("Error getting VM Info from SPP");
		sppLogOut(regInfo.getSppHost(), session);
		return "Error getting VM Info";
	}

	// Gets OS dependent path for registration config file
	public String getRegistrationInfoPath() {
		String path;
		if (SystemUtils.IS_OS_WINDOWS) {
			path = System.getenv("PROGRAMDATA") + "\\VMWare\\vCenterServer\\spp";
		} else if (SystemUtils.IS_OS_MAC) {
			path = "/var/lib/spp";
		} else {
			path = "/storage/spp";
		}
		return path;
	}
	
	// Gets OS dependent full path for registration config file (includes filename)
	public String getRegistrationInfoFilePath() {
		String path;
		if (SystemUtils.IS_OS_WINDOWS) {
			path = System.getenv("PROGRAMDATA") + "\\VMWare\\vCenterServer\\spp\\regInfo.config";
		} else if (SystemUtils.IS_OS_MAC) {
			path = "/var/lib/spp/regInfo.config";
		} else {
			path = "/storage/spp/regInfo.config";
		}
		return path;
	}
	
	// Save SPP registration config to vCenterServer folder
	// File is simple overwritten if it already exists
	// Reg info is stored as JSON
	public void setSppRegistrationInfo(String regInfo) {
		String path = getRegistrationInfoPath();
		String filePath = getRegistrationInfoFilePath();
		try {
			File info = new File(path);
			File config = new File(filePath);
			if(!info.exists()) {
				info.mkdirs();
				config.createNewFile();
			}
			BufferedWriter writer = new BufferedWriter(new FileWriter(config));
			writer.write(regInfo);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// Get SPP config data
	// Returns as a RegistrationInfo object
	@Override
	public RegistrationInfo getSppRegistrationInfo() {
		String filePath = getRegistrationInfoFilePath();
		String config;
		try {
			FileReader reader = new FileReader(filePath);
			BufferedReader br = new BufferedReader(reader);
			config = br.readLine();
			br.close();
			return new Gson().fromJson(config, RegistrationInfo.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	// Logs in to SPP
	// Returns SppSession object containing the session id for other SPP API requests
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
			log.info("Logged in to SPP");
			return sppSession;
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.error("Error logging in to SPP");
		return new SppSession();
	}

	// Logs out of SPP
	public void sppLogOut(String host, SppSession session) {
		String sppSesUrl = host + SppUrls.sppSessionUrl;
		String sesIdHdr = session.getSessionId();
		try {
			CloseableHttpClient httpclient = createAcceptSelfSignedCertificateClient();
			HttpUriRequest request = RequestBuilder.delete().setUri(sppSesUrl).build();
			request.setHeader("X-Endeavour-Sessionid", sesIdHdr);
			HttpResponse response = httpclient.execute(request);
			if (response.getStatusLine().getStatusCode() != 204) {
				log.error("Error logging out of SPP");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
