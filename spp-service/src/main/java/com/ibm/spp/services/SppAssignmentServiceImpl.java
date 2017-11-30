package com.ibm.spp.services;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ibm.spp.domain.RegistrationInfo;
import com.ibm.spp.domain.SppAssignment;
import com.ibm.spp.domain.SppAssignmentResources;
import com.ibm.spp.domain.SppAssignmentSlapols;
import com.ibm.spp.domain.SppSession;
import com.ibm.spp.domain.SppUrls;

public class SppAssignmentServiceImpl implements SppAssignmentService {
	
	public SppAssignmentServiceImpl() {}
	
	private static final Log log = LogFactory.getLog(SppAssignmentServiceImpl.class);
	
	SppRegistrationService sppRegistrationService = new SppRegistrationServiceImpl();
	SppInfoService sppInfoService = new SppInfoServiceImpl();

	// Do POST for folder or vm assignment
	@Override
	public String postAssignmentData(String assignBody) {
		RegistrationInfo regInfo = sppRegistrationService.getSppRegistrationInfo();
		String assignUrl = regInfo.getSppHost() + SppUrls.sppAssignUrl;
		SppSession session = sppRegistrationService.sppLogIn(regInfo.getSppHost(), regInfo.getSppUser(), regInfo.getSppPass());
		try {
			CloseableHttpClient httpclient = SelfSignedHttpsClient.createAcceptSelfSignedCertificateClient();
			HttpPost request = new HttpPost(assignUrl);
			request.setHeader("X-Endeavour-Sessionid", session.sessionid);
			request.setHeader("Accept", "application/json");
			request.setHeader("Content-Type", "application/json");
			StringEntity body = new StringEntity(assignBody);
			request.setEntity(body);
			HttpResponse response = httpclient.execute(request);
			HttpEntity entity = response.getEntity();
			String responseString = EntityUtils.toString(entity, "UTF-8");
			log.info("Assinging Resource to SLA");
			sppRegistrationService.sppLogOut(regInfo.getSppHost(), session);
			return responseString;
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.error("Error Assigning Resource to SLA");
		log.error("Body: " + assignBody);
		sppRegistrationService.sppLogOut(regInfo.getSppHost(), session);
		return "Error Assigning Resource to SLA";
	}

	@Override
	public SppAssignment buildAssignmentDataVm(String vmName, List<String> slaNameList) {
		SppAssignment assignData = new SppAssignment();
		assignData.setResources(buildResourceDataVm(vmName));
		assignData.setSlapolicies(buildSlaData(slaNameList));
		return assignData;
	}
	
	@Override
	public SppAssignment buildAssignmentDataFolder(String folderName, List<String> slaNameList) {
		SppAssignment assignData = new SppAssignment();
		assignData.setResources(buildResourceDataFolder(folderName));
		assignData.setSlapolicies(buildSlaData(slaNameList));
		return assignData;
	}
	
	private List<SppAssignmentResources> buildResourceDataVm(String vmName) {
		List<SppAssignmentResources> assignmentResources = new ArrayList<SppAssignmentResources>();
		String sppVm = sppInfoService.getSppVmInfo(vmName);
		JsonObject sppVmJson = (JsonObject) new JsonParser().parse(sppVm);
		SppAssignmentResources resource = new SppAssignmentResources();
		resource.setId(sppVmJson.get("id").getAsString());
		resource.setMetadataPath(sppVmJson.get("metadataPath").getAsString());
		resource.setHref(
				sppVmJson.get("links").getAsJsonObject().get("self").getAsJsonObject().get("href").getAsString());
		assignmentResources.add(resource);
		return assignmentResources;
	}

	private List<SppAssignmentSlapols> buildSlaData(List<String> slaNameList) {
		List<SppAssignmentSlapols> assignmentSlas = new ArrayList<SppAssignmentSlapols>();
		String sppSlas = sppInfoService.getSlaPolicies();
		JsonArray sppSlaArray = (JsonArray) new JsonParser().parse(sppSlas);
		for (int i = 0; i < sppSlaArray.size(); i++) {
			JsonObject sla = sppSlaArray.get(i).getAsJsonObject();
			String slaName = sla.get("name").getAsString();
			if (slaNameList.contains(slaName)) {
				// not deserializing directly because of nested href value
				SppAssignmentSlapols sppAssignSla = new SppAssignmentSlapols();
				sppAssignSla.setId(sla.get("id").getAsString());
				sppAssignSla.setName(sla.get("name").getAsString());
				sppAssignSla.setHref(
						sla.get("links").getAsJsonObject().get("self").getAsJsonObject().get("href").getAsString());
				assignmentSlas.add(sppAssignSla);
			}
		}
		return assignmentSlas;
	}

	private List<SppAssignmentResources> buildResourceDataFolder(String folderName) {
		List<SppAssignmentResources> assignmentResources = new ArrayList<SppAssignmentResources>();
		String sppFolder = sppInfoService.getSppFolderInfo(folderName);
		JsonObject sppFolderJson = (JsonObject) new JsonParser().parse(sppFolder);
		SppAssignmentResources resource = new SppAssignmentResources();
		resource.setId(sppFolderJson.get("id").getAsString());
		resource.setMetadataPath(sppFolderJson.get("metadataPath").getAsString());
		resource.setHref(
				sppFolderJson.get("links").getAsJsonObject().get("self").getAsJsonObject().get("href").getAsString());
		assignmentResources.add(resource);
		return assignmentResources;
	}
}
