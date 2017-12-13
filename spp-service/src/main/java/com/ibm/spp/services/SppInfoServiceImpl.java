package com.ibm.spp.services;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ibm.spp.domain.RegistrationInfo;
import com.ibm.spp.domain.SppSession;
import com.ibm.spp.domain.SppUrls;

public class SppInfoServiceImpl implements SppInfoService {
	
	public SppInfoServiceImpl() {}
	
	private static final Log log = LogFactory.getLog(SppInfoServiceImpl.class);
	
	// Get JSON for all SLA policies in SPP
	@Override
	public String getSlaPolicies(SppSession session) {
		String slaUrl = session.getHost() + SppUrls.sppSlaUrl;
		try {
			CloseableHttpClient httpclient = SelfSignedHttpsClient.createAcceptSelfSignedCertificateClient();
			HttpUriRequest request = RequestBuilder.get().setUri(slaUrl).build();
			request.setHeader("X-Endeavour-Sessionid", session.sessionid);
			HttpResponse response = httpclient.execute(request);
			HttpEntity entity = response.getEntity();
			String responseString = EntityUtils.toString(entity, "UTF-8");
			log.info("Returning SLA Policies from SPP");
			JsonObject slaJsonAll = (JsonObject) new JsonParser().parse(responseString);
			JsonArray slaJsonArray = (JsonArray) slaJsonAll.get("slapolicies");
			return slaJsonArray.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.error("Error getting SLA Policies from SPP");
		return "Error getting SLA Policies";
	}

	// Get JSON for VM data from SPP
	// Uses the search API
	// This is a POST here but a GET in our controller
	@Override
	public String getSppVmInfo(String vmName, SppSession session) {
		String vmUrl = session.getHost() + SppUrls.sppVmUrl;
		JsonObject searchJO = new JsonObject();
		searchJO.addProperty("name", vmName);
		searchJO.addProperty("hypervisorType", "vmware");
		try {
			CloseableHttpClient httpclient = SelfSignedHttpsClient.createAcceptSelfSignedCertificateClient();
			HttpPost request = new HttpPost(vmUrl);
			request.setHeader("X-Endeavour-Sessionid", session.sessionid);
			request.setHeader("Accept", "application/json");
			request.setHeader("Content-Type", "application/json");
			StringEntity body = new StringEntity(searchJO.toString());
			request.setEntity(body);
			HttpResponse response = httpclient.execute(request);
			HttpEntity entity = response.getEntity();
			String responseString = EntityUtils.toString(entity, "UTF-8");
			log.info("Returning VM Info from SPP");
			JsonObject sppVmJsonAll = (JsonObject) new JsonParser().parse(responseString);
			JsonArray sppVmJsonArray = (JsonArray) sppVmJsonAll.get("vms");
			// need to loop thru results here as search API could return more
			// than one match
			for (int i = 0; i < sppVmJsonArray.size(); i++) {
				JsonObject vm = sppVmJsonArray.get(i).getAsJsonObject();
				String vmNameFromSearch = vm.get("name").getAsString();
				if (vmNameFromSearch.equals(vmName)) {
					return sppVmJsonArray.get(i).toString();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.error("Error getting VM Info from SPP");
		return "Error getting VM Info";
	}
	
	// Same as above but uses restore clause in url
	@Override
	public String getSppVmInfoForRestore(String vmName, SppSession session) {
		String vmUrl = session.getHost() + SppUrls.sppVmRestoreUrl;
		JsonObject searchJO = new JsonObject();
		searchJO.addProperty("name", vmName);
		searchJO.addProperty("hypervisorType", "vmware");
		try {
			CloseableHttpClient httpclient = SelfSignedHttpsClient.createAcceptSelfSignedCertificateClient();
			HttpPost request = new HttpPost(vmUrl);
			request.setHeader("X-Endeavour-Sessionid", session.sessionid);
			request.setHeader("Accept", "application/json");
			request.setHeader("Content-Type", "application/json");
			StringEntity body = new StringEntity(searchJO.toString());
			request.setEntity(body);
			HttpResponse response = httpclient.execute(request);
			HttpEntity entity = response.getEntity();
			String responseString = EntityUtils.toString(entity, "UTF-8");
			log.info("Returning VM Info from SPP");
			JsonObject sppVmJsonAll = (JsonObject) new JsonParser().parse(responseString);
			JsonArray sppVmJsonArray = (JsonArray) sppVmJsonAll.get("vms");
			// need to loop thru results here as search API could return more
			// than one match
			for (int i = 0; i < sppVmJsonArray.size(); i++) {
				JsonObject vm = sppVmJsonArray.get(i).getAsJsonObject();
				String vmNameFromSearch = vm.get("name").getAsString();
				if (vmNameFromSearch.equals(vmName)) {
					return sppVmJsonArray.get(i).toString();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.error("Error getting VM Info from SPP");
		return "Error getting VM Info";
	}

	// Get JSON for FOLDER data from SPP
	// Uses the search API
	// This is a POST here but a GET in our controller
	@Override
	public String getSppFolderInfo(String folderName, SppSession session) {
		String folderUrl = session.getHost() + SppUrls.sppFolderUrl;
		JsonObject searchJO = new JsonObject();
		searchJO.addProperty("name", folderName);
		searchJO.addProperty("hypervisorType", "vmware");
		try {
			CloseableHttpClient httpclient = SelfSignedHttpsClient.createAcceptSelfSignedCertificateClient();
			HttpPost request = new HttpPost(folderUrl);
			request.setHeader("X-Endeavour-Sessionid", session.sessionid);
			request.setHeader("Accept", "application/json");
			request.setHeader("Content-Type", "application/json");
			StringEntity body = new StringEntity(searchJO.toString());
			request.setEntity(body);
			HttpResponse response = httpclient.execute(request);
			HttpEntity entity = response.getEntity();
			String responseString = EntityUtils.toString(entity, "UTF-8");
			log.info("Returning VM Info from SPP");
			JsonObject sppFolderJsonAll = (JsonObject) new JsonParser().parse(responseString);
			JsonArray sppFolderJsonArray = (JsonArray) sppFolderJsonAll.get("folders");
			// need loop here to match name exactly in case search returns
			// multiple results
			for (int i = 0; i < sppFolderJsonArray.size(); i++) {
				JsonObject vm = sppFolderJsonArray.get(i).getAsJsonObject();
				String vmNameFromSearch = vm.get("name").getAsString();
				if (vmNameFromSearch.equals(folderName)) {
					return sppFolderJsonArray.get(i).toString();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.error("Error getting Folder Info from SPP");
		return "Error getting Folder Info";
	}
	
	@Override
	public String getSppActiveRestoreSessions(SppSession session) {
		String restoreSessionUrl = session.getHost() + SppUrls.sppJobSessionUrl;
		try {
			CloseableHttpClient httpclient = SelfSignedHttpsClient.createAcceptSelfSignedCertificateClient();
			HttpUriRequest request = RequestBuilder.get().setUri(restoreSessionUrl).build();
			request.setHeader("X-Endeavour-Sessionid", session.sessionid);
			HttpResponse response = httpclient.execute(request);
			HttpEntity entity = response.getEntity();
			String responseString = EntityUtils.toString(entity, "UTF-8");
			log.info("Returning Active Restore Sessions from SPP");
			JsonObject responseJsonAll = (JsonObject) new JsonParser().parse(responseString);
			JsonArray responseSessions = (JsonArray) responseJsonAll.get("sessions");
			return responseSessions.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.error("Error getting Active Restore Sessions from SPP");
		return "Error getting Active Restore Sessions";
	}

	@Override
	public String getDashboardInfo(SppSession session) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSppVmVersions(SppSession session, String vmid, String hvid) {
		String vmVersUrl = session.getHost() + SppUrls.sppVmVersionUrl.replace("HVID", hvid).replaceAll("VMID", vmid);
		try {
			CloseableHttpClient httpclient = SelfSignedHttpsClient.createAcceptSelfSignedCertificateClient();
			HttpUriRequest request = RequestBuilder.get().setUri(vmVersUrl).build();
			request.setHeader("X-Endeavour-Sessionid", session.sessionid);
			HttpResponse response = httpclient.execute(request);
			HttpEntity entity = response.getEntity();
			String responseString = EntityUtils.toString(entity, "UTF-8");
			log.info("Returning VM Versions from SPP");
			JsonObject responseJsonAll = (JsonObject) new JsonParser().parse(responseString);
			JsonArray responseSessions = (JsonArray) responseJsonAll.get("versions");
			return responseSessions.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.error("Error getting VM Versions from SPP");
		return "Error getting VM Versions";
	}

	@Override
	public String getSppFolderVersions(SppSession session, String folderid, String hvid) {
		String folderVersUrl = session.getHost() + SppUrls.sppFolderVersionUrl.replace("HVID", hvid).replaceAll("FOLDERID", folderid);
		try {
			CloseableHttpClient httpclient = SelfSignedHttpsClient.createAcceptSelfSignedCertificateClient();
			HttpUriRequest request = RequestBuilder.get().setUri(folderVersUrl).build();
			request.setHeader("X-Endeavour-Sessionid", session.sessionid);
			HttpResponse response = httpclient.execute(request);
			HttpEntity entity = response.getEntity();
			String responseString = EntityUtils.toString(entity, "UTF-8");
			log.info("Returning Folder Versions from SPP");
			JsonObject responseJsonAll = (JsonObject) new JsonParser().parse(responseString);
			JsonArray responseSessions = (JsonArray) responseJsonAll.get("versions");
			return responseSessions.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.error("Error getting Folder Versions from SPP");
		return "Error getting Folder Versions";
	}
}
