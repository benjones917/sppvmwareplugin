package com.ibm.spp.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
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

	public SppAssignmentServiceImpl() {
	}

	private static final Log log = LogFactory.getLog(SppAssignmentServiceImpl.class);

	SppInfoService sppInfoService = new SppInfoServiceImpl();

	// Assign sla policy to vm
	// Takes a String of VM and List of String SLA policy names
	// UI should pass in any existing SLA policy names that aren't removed
	@Override
	public String assignVmToSla(String vmName, String vmId, String slaName, SppSession session) {
		List<String> slaNameList = new ArrayList<String>();
		slaNameList = Arrays.asList(slaName.split(","));
		SppAssignment assignData = buildAssignmentDataVm(vmName, vmId, slaNameList, session);
		String assignBody = new Gson().toJson(assignData);
		String postResponse = postAssignmentData(assignBody, session);
		return postResponse;
	}

	// Same as above but for folder
	@Override
	public String assignFolderToSla(String folderName, String groupId, String slaName, SppSession session) {
		List<String> slaNameList = new ArrayList<String>();
		slaNameList = Arrays.asList(slaName.split(","));
		SppAssignment assignData = buildAssignmentDataFolder(folderName, groupId, slaNameList, session);
		String assignBody = new Gson().toJson(assignData);
		String postResponse = postAssignmentData(assignBody, session);
		return postResponse;
	}

	// Do POST for folder or vm assignment
	private String postAssignmentData(String assignBody, SppSession session) {
		String assignUrl = session.getHost() + SppUrls.sppAssignUrl;
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
			return responseString;
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.error("Error Assigning Resource to SLA");
		log.error("Body: " + assignBody);
		return "Error Assigning Resource to SLA";
	}

	private SppAssignment buildAssignmentDataVm(String vmName, String vmId, List<String> slaNameList, SppSession session) {
		SppAssignment assignData = new SppAssignment();
		assignData.setResources(buildResourceDataVm(vmName, vmId, session));
		assignData.setSlapolicies(buildSlaData(slaNameList, session));
		return assignData;
	}

	private SppAssignment buildAssignmentDataFolder(String folderName, String groupId, List<String> slaNameList, SppSession session) {
		SppAssignment assignData = new SppAssignment();
		assignData.setResources(buildResourceDataFolder(folderName, groupId, session));
		assignData.setSlapolicies(buildSlaData(slaNameList, session));
		return assignData;
	}

	private List<SppAssignmentResources> buildResourceDataVm(String vmName, String vmId, SppSession session) {
		List<SppAssignmentResources> assignmentResources = new ArrayList<SppAssignmentResources>();
		String sppVm = sppInfoService.getSppVmInfo(vmName, vmId, session);
		JsonObject sppVmJson = (JsonObject) new JsonParser().parse(sppVm);
		SppAssignmentResources resource = new SppAssignmentResources();
		resource.setId(sppVmJson.get("id").getAsString());
		resource.setMetadataPath(sppVmJson.get("metadataPath").getAsString());
		resource.setHref(
				sppVmJson.get("links").getAsJsonObject().get("self").getAsJsonObject().get("href").getAsString());
		assignmentResources.add(resource);
		return assignmentResources;
	}

	private List<SppAssignmentSlapols> buildSlaData(List<String> slaNameList, SppSession session) {
		List<SppAssignmentSlapols> assignmentSlas = new ArrayList<SppAssignmentSlapols>();
		String sppSlas = sppInfoService.getSlaPolicies(session);
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

	private List<SppAssignmentResources> buildResourceDataFolder(String folderName, String groupId, SppSession session) {
		List<SppAssignmentResources> assignmentResources = new ArrayList<SppAssignmentResources>();
		String sppFolder = sppInfoService.getSppFolderInfo(folderName, groupId, session);
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
