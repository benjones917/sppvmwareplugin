package com.ibm.spp.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ibm.spp.domain.SppRestore;
import com.ibm.spp.domain.SppRestoreOption;
import com.ibm.spp.domain.SppRestoreSource;
import com.ibm.spp.domain.SppRestoreSpec;
import com.ibm.spp.domain.SppRestoreSubpolicy;
import com.ibm.spp.domain.SppRestoreVersion;
import com.ibm.spp.domain.SppRestoreVersionMetadata;
import com.ibm.spp.domain.SppSession;
import com.ibm.spp.domain.SppUrls;

public class SppRestoreServiceImpl implements SppRestoreService {
	
	private static final Log log = LogFactory.getLog(SppServiceImpl.class);
	
	SppInfoService sppInfoService = new SppInfoServiceImpl();
	
	@Override
	public String restoreLatestVmTest(String vmName, String vmId, SppSession session) {
		String postJsonData = buildRestoreDataLatestTest(vmName, vmId, session);
		String postResponse = postRestoreData(postJsonData, session);
		return postResponse;
	}
	
	@Override
	public String restoreLatestVmProd(String vmName, String vmId, SppSession session) {
		String postJsonData = buildRestoreDataLatestProd(vmName, vmId, session);
		String postResponse = postRestoreData(postJsonData, session);
		return postResponse;
	}
	
	@Override
	public String restoreVersionVmTest(String vmName, String vmId, String version, SppSession session) {
		String postJsonData = buildRestoreDataVersionTest(vmName, vmId, version, session);
		String postResponse = postRestoreData(postJsonData, session);
		return postResponse;
	}
	
	@Override
	public String restoreVersionVmProd(String vmName, String vmId, String version, SppSession session) {
		String postJsonData = buildRestoreDataVersionProd(vmName, vmId, version, session);
		String postResponse = postRestoreData(postJsonData, session);
		return postResponse;
	}
	
	private String postRestoreData(String postJsonData, SppSession session) {
		String restoreUrl = session.getHost() + SppUrls.sppVmRestoreActionUrl;
		try {
			CloseableHttpClient httpclient = SelfSignedHttpsClient.createAcceptSelfSignedCertificateClient();
			HttpPost request = new HttpPost(restoreUrl);
			request.setHeader("X-Endeavour-Sessionid", session.sessionid);
			request.setHeader("Accept", "application/json");
			request.setHeader("Content-Type", "application/json");
			StringEntity body = new StringEntity(postJsonData);
			request.setEntity(body);
			HttpResponse response = httpclient.execute(request);
			HttpEntity entity = response.getEntity();
			String responseString = EntityUtils.toString(entity, "UTF-8");
			log.info("Restoring VM");
			return responseString;
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.error("Error Restoring VM");
		log.error("Body: " + postJsonData);
		return "Error Restoring VM";
	}

	private String buildRestoreDataLatestTest(String vmName, String vSphereVmId, SppSession session) {
		String vmData = sppInfoService.getSppVmInfoForRestore(vmName, vSphereVmId, session);
		JsonParser parser = new JsonParser();
		JsonObject vmDataJson = parser.parse(vmData).getAsJsonObject();
		
		String vmHref = vmDataJson.get("links").getAsJsonObject().get("self").getAsJsonObject().get("href").getAsString();
		String versionHref = vmDataJson.get("links").getAsJsonObject().get("latestversion").getAsJsonObject().get("href").getAsString();
		String vmInfoName = vmDataJson.get("name").getAsString();
		String vmId = vmDataJson.get("id").getAsString();
		
		SppRestore sppRestore = new SppRestore();
		SppRestoreSpec sppRestoreSpec = new SppRestoreSpec();
		SppRestoreSource sppRestoreSource = new SppRestoreSource();
		SppRestoreOption sppRestoreOption = new SppRestoreOption();
		SppRestoreSubpolicy sppRestoreSubpolicy = new SppRestoreSubpolicy();
		SppRestoreVersion sppRestoreVersion = new SppRestoreVersion();
		SppRestoreVersionMetadata sppRestoreVersionMetadata = new SppRestoreVersionMetadata();
		
		sppRestoreVersionMetadata.setUseLatest(true);
		sppRestoreVersionMetadata.setName("Use Latest");
		
		List<SppRestoreSource> sppRestoreSourceList = new ArrayList<SppRestoreSource>();
		List<SppRestoreSubpolicy> sppRestoreSubpolicyList = new ArrayList<SppRestoreSubpolicy>();
		
		sppRestoreVersion.setMetadata(sppRestoreVersionMetadata);
		sppRestoreVersion.setHref(versionHref);
		
		sppRestoreSource.setVersion(sppRestoreVersion);
		sppRestoreSource.setHref(vmHref);
		Map<String,String> mdMap = new HashMap<String,String>();
		mdMap.put("name", vmInfoName);
		sppRestoreSource.setMetadata(mdMap);
		sppRestoreSource.setId(vmId);
		
		sppRestoreOption.setMode("test");
		sppRestoreOption.setAllowvmoverwrite(false);
		sppRestoreSubpolicy.setType("IV");
		sppRestoreSubpolicy.setOption(sppRestoreOption);
		
		sppRestoreSourceList.add(sppRestoreSource);
		sppRestoreSubpolicyList.add(sppRestoreSubpolicy);
		
		sppRestoreSpec.setSource(sppRestoreSourceList);
		sppRestoreSpec.setSubpolicy(sppRestoreSubpolicyList);
		sppRestore.setSpec(sppRestoreSpec);
		
		String body = new Gson().toJson(sppRestore);
		return body;
	}
	
	private String buildRestoreDataLatestProd(String vmName, String vSphereVmId, SppSession session) {
		String vmData = sppInfoService.getSppVmInfoForRestore(vmName, vSphereVmId, session);
		JsonParser parser = new JsonParser();
		JsonObject vmDataJson = parser.parse(vmData).getAsJsonObject();
		
		String vmHref = vmDataJson.get("links").getAsJsonObject().get("self").getAsJsonObject().get("href").getAsString();
		String versionHref = vmDataJson.get("links").getAsJsonObject().get("latestversion").getAsJsonObject().get("href").getAsString();
		String vmInfoName = vmDataJson.get("name").getAsString();
		String vmId = vmDataJson.get("id").getAsString();
		
		SppRestore sppRestore = new SppRestore();
		SppRestoreSpec sppRestoreSpec = new SppRestoreSpec();
		SppRestoreSource sppRestoreSource = new SppRestoreSource();
		SppRestoreOption sppRestoreOption = new SppRestoreOption();
		SppRestoreSubpolicy sppRestoreSubpolicy = new SppRestoreSubpolicy();
		SppRestoreVersion sppRestoreVersion = new SppRestoreVersion();
		SppRestoreVersionMetadata sppRestoreVersionMetadata = new SppRestoreVersionMetadata();
		
		sppRestoreVersionMetadata.setUseLatest(true);
		sppRestoreVersionMetadata.setName("Use Latest");
		
		List<SppRestoreSource> sppRestoreSourceList = new ArrayList<SppRestoreSource>();
		List<SppRestoreSubpolicy> sppRestoreSubpolicyList = new ArrayList<SppRestoreSubpolicy>();
		
		sppRestoreVersion.setMetadata(sppRestoreVersionMetadata);
		sppRestoreVersion.setHref(versionHref);
		
		sppRestoreSource.setVersion(sppRestoreVersion);
		sppRestoreSource.setHref(vmHref);
		Map<String,String> mdMap = new HashMap<String,String>();
		mdMap.put("name", vmInfoName);
		sppRestoreSource.setMetadata(mdMap);
		sppRestoreSource.setId(vmId);
		
		sppRestoreOption.setMode("recovery");
		sppRestoreOption.setAllowvmoverwrite(true);
		sppRestoreSubpolicy.setType("IV");
		sppRestoreSubpolicy.setOption(sppRestoreOption);
		
		sppRestoreSourceList.add(sppRestoreSource);
		sppRestoreSubpolicyList.add(sppRestoreSubpolicy);
		
		sppRestoreSpec.setSource(sppRestoreSourceList);
		sppRestoreSpec.setSubpolicy(sppRestoreSubpolicyList);
		sppRestore.setSpec(sppRestoreSpec);
		
		String body = new Gson().toJson(sppRestore);
		return body;
	}
	
	private SppRestoreVersion buildVersionData(String version) {
		SppRestoreVersion sppRestoreVersion = new SppRestoreVersion();
		SppRestoreVersionMetadata sppRestoreVersionMetadata = new SppRestoreVersionMetadata();
		JsonParser parser = new JsonParser();
		JsonObject versionJson = parser.parse(version).getAsJsonObject();
		
		String href = versionJson.get("links").getAsJsonObject().get("self").getAsJsonObject().get("href").getAsString();
		String protectionTime = versionJson.get("protectionTime").getAsString();
		
		sppRestoreVersion.setHref(href);
		sppRestoreVersionMetadata.setUseLatest(false);
		sppRestoreVersionMetadata.setProtectionTime(protectionTime);
		sppRestoreVersion.setMetadata(sppRestoreVersionMetadata);
		return sppRestoreVersion;
	}
	
	private String buildRestoreDataVersionTest(String vmName, String vSphereVmId, String version, SppSession session) {
		String vmData = sppInfoService.getSppVmInfoForRestore(vmName, vSphereVmId, session);
		JsonParser parser = new JsonParser();
		JsonObject vmDataJson = parser.parse(vmData).getAsJsonObject();
		
		String vmHref = vmDataJson.get("links").getAsJsonObject().get("self").getAsJsonObject().get("href").getAsString();
		String vmInfoName = vmDataJson.get("name").getAsString();
		String vmId = vmDataJson.get("id").getAsString();
		
		SppRestore sppRestore = new SppRestore();
		SppRestoreSpec sppRestoreSpec = new SppRestoreSpec();
		SppRestoreSource sppRestoreSource = new SppRestoreSource();
		SppRestoreOption sppRestoreOption = new SppRestoreOption();
		SppRestoreSubpolicy sppRestoreSubpolicy = new SppRestoreSubpolicy();
		SppRestoreVersion sppRestoreVersion = buildVersionData(version);
		
		List<SppRestoreSource> sppRestoreSourceList = new ArrayList<SppRestoreSource>();
		List<SppRestoreSubpolicy> sppRestoreSubpolicyList = new ArrayList<SppRestoreSubpolicy>();
		
		sppRestoreSource.setVersion(sppRestoreVersion);
		sppRestoreSource.setHref(vmHref);
		Map<String,String> mdMap = new HashMap<String,String>();
		mdMap.put("name", vmInfoName);
		sppRestoreSource.setMetadata(mdMap);
		sppRestoreSource.setId(vmId);
		
		sppRestoreOption.setMode("test");
		sppRestoreOption.setAllowvmoverwrite(false);
		sppRestoreSubpolicy.setType("IV");
		sppRestoreSubpolicy.setOption(sppRestoreOption);
		
		sppRestoreSourceList.add(sppRestoreSource);
		sppRestoreSubpolicyList.add(sppRestoreSubpolicy);
		
		sppRestoreSpec.setSource(sppRestoreSourceList);
		sppRestoreSpec.setSubpolicy(sppRestoreSubpolicyList);
		sppRestore.setSpec(sppRestoreSpec);
		
		String body = new Gson().toJson(sppRestore);
		return body;
	}
	
	private String buildRestoreDataVersionProd(String vmName, String vSphereVmId, String version, SppSession session) {
		String vmData = sppInfoService.getSppVmInfoForRestore(vmName, vSphereVmId, session);
		JsonParser parser = new JsonParser();
		JsonObject vmDataJson = parser.parse(vmData).getAsJsonObject();
		
		String vmHref = vmDataJson.get("links").getAsJsonObject().get("self").getAsJsonObject().get("href").getAsString();
		String vmInfoName = vmDataJson.get("name").getAsString();
		String vmId = vmDataJson.get("id").getAsString();
		
		SppRestore sppRestore = new SppRestore();
		SppRestoreSpec sppRestoreSpec = new SppRestoreSpec();
		SppRestoreSource sppRestoreSource = new SppRestoreSource();
		SppRestoreOption sppRestoreOption = new SppRestoreOption();
		SppRestoreSubpolicy sppRestoreSubpolicy = new SppRestoreSubpolicy();
		SppRestoreVersion sppRestoreVersion = buildVersionData(version);
		
		List<SppRestoreSource> sppRestoreSourceList = new ArrayList<SppRestoreSource>();
		List<SppRestoreSubpolicy> sppRestoreSubpolicyList = new ArrayList<SppRestoreSubpolicy>();
		
		sppRestoreSource.setVersion(sppRestoreVersion);
		sppRestoreSource.setHref(vmHref);
		Map<String,String> mdMap = new HashMap<String,String>();
		mdMap.put("name", vmInfoName);
		sppRestoreSource.setMetadata(mdMap);
		sppRestoreSource.setId(vmId);
		
		sppRestoreOption.setMode("recovery");
		sppRestoreOption.setAllowvmoverwrite(true);
		sppRestoreSubpolicy.setType("IV");
		sppRestoreSubpolicy.setOption(sppRestoreOption);
		
		sppRestoreSourceList.add(sppRestoreSource);
		sppRestoreSubpolicyList.add(sppRestoreSubpolicy);
		
		sppRestoreSpec.setSource(sppRestoreSourceList);
		sppRestoreSpec.setSubpolicy(sppRestoreSubpolicyList);
		sppRestore.setSpec(sppRestoreSpec);
		
		String body = new Gson().toJson(sppRestore);
		return body;
	}

}
