package com.ibm.spp.services;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Base64;

import org.apache.commons.lang3.SystemUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.ibm.spp.domain.RegistrationInfo;
import com.ibm.spp.domain.SppSession;
import com.ibm.spp.domain.SppUrls;
import com.ibm.spp.domain.SppVcenterRegistration;

public class SppRegistrationServiceImpl implements SppRegistrationService {
	
	public SppRegistrationServiceImpl() {}
	
	private static final Log log =
	         LogFactory.getLog(SppServiceImpl.class);
	
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
		log.error("Error getting config");
		return null;
	}

	// Save SPP registration config to vCenterServer folder
	// File is simple overwritten if it already exists
	// Reg info is stored as JSON
	@Override
	public void setSppRegistrationInfo(String regInfo) {
		String path = getRegistrationInfoPath();
		String filePath = getRegistrationInfoFilePath();
		try {
			File info = new File(path);
			File config = new File(filePath);
			if (!info.exists()) {
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

	// Logs in to SPP
	// Returns SppSession object containing the session id for other SPP API
	// requests
	@Override
	public SppSession sppLogIn(RegistrationInfo regInfo) {
		String sppSesUrl = regInfo.getSppHost() + SppUrls.sppSessionUrl;
		String userPassText = regInfo.getSppUser() + ":" + regInfo.getSppPass();
		String b64UserPass = new String(Base64.getEncoder().encode(userPassText.getBytes()));
		try {
			CloseableHttpClient httpclient = SelfSignedHttpsClient.createAcceptSelfSignedCertificateClient();
			HttpUriRequest request = RequestBuilder.post().setUri(sppSesUrl).build();
			request.setHeader("Authorization", "Basic " + b64UserPass);
			HttpResponse response = httpclient.execute(request);
			HttpEntity entity = response.getEntity();
			String responseString = EntityUtils.toString(entity, "UTF-8");
			SppSession sppSession = new Gson().fromJson(responseString, SppSession.class);
			sppSession.setHost(regInfo.getSppHost());
			sppSession.setUser(regInfo.getSppUser());
			log.info("Logged in to SPP");
			return sppSession;
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.error("Error logging in to SPP");
		return new SppSession();
	}

	// Logs out of SPP
	@Override
	public void sppLogOut(SppSession session) {
		String sppSesUrl = session.getHost() + SppUrls.sppSessionUrl;
		String sesIdHdr = session.getSessionId();
		try {
			CloseableHttpClient httpclient = SelfSignedHttpsClient.createAcceptSelfSignedCertificateClient();
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
	
	// Gets OS dependent path for registration config file
	private String getRegistrationInfoPath() {
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

	// Gets OS dependent full path for registration config file (includes
	// filename)
	private String getRegistrationInfoFilePath() {
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

	@Override
	public String registerVcenter(String vcRegInfo) {
		RegistrationInfo regInfo = getSppRegistrationInfo();
		SppSession session = sppLogIn(regInfo);
		SppVcenterRegistration vcRegObj = new Gson().fromJson(vcRegInfo, SppVcenterRegistration.class);
		// set currently hardcoded variables (which we don't want to manage in plugin UI)
		// reserialize
		// make registration POST to /spp/ecxngp/hypervisor
		// return proper response String
		return null;
	}

	@Override
	public String getVcRegistration(String vcId) {
		// make GET call to /spp/ecxngp/hypervisor (returns list of registered hypervisors)
		// loop through and compare response attr uniqueId to passed in vcenter ID
		// if we find that return the record, else return indication that vcenter not yet registered
		return null;
	}
}
