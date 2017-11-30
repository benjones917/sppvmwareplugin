package com.ibm.spp.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.google.gson.Gson;
import com.ibm.spp.domain.RegistrationInfo;
import com.ibm.spp.domain.SppAssignment;
import com.ibm.spp.domain.SppSession;

public class SppServiceImpl implements SppService {
	
	private static final Log log = LogFactory.getLog(SppServiceImpl.class);
	
	SppRegistrationService sppRegistrationService = new SppRegistrationServiceImpl();
	SppInfoService sppInfoService = new SppInfoServiceImpl();
	SppAssignmentService sppAssignmentService = new SppAssignmentServiceImpl();
	
	// Main function to register SPP
	// Saving of registration is dependent on successful login
	// Currently returns String response of status
	@Override
	public String register(String registrationInfo) {
		RegistrationInfo regInfo = new Gson().fromJson(registrationInfo, RegistrationInfo.class);
		SppSession session = sppRegistrationService.sppLogIn(regInfo.getSppHost(), regInfo.getSppUser(), regInfo.getSppPass());
		if(session.getSessionId() != null) {
			sppRegistrationService.sppLogOut(regInfo.getSppHost(), session);
			sppRegistrationService.setSppRegistrationInfo(registrationInfo);
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
		return sppInfoService.getSlaPolicies();
	}
	
	// Get JSON for VM data from SPP
	// Uses the search API
	// This is a POST here but a GET in our controller
	@Override 
	public String getSppVmInfo(String vmName) {
		return sppInfoService.getSppVmInfo(vmName);
	}

	// Get JSON for FOLDER data from SPP
	// Uses the search API
	// This is a POST here but a GET in our controller
	@Override
	public String getSppFolderInfo(String folderName) {
		return sppInfoService.getSppFolderInfo(folderName);
	}

	@Override
	public RegistrationInfo getSppRegistrationInfo() {
		return sppRegistrationService.getSppRegistrationInfo();
	}

	// Assign sla policy to vm
	// Takes a String of VM and List of String SLA policy names
	// UI should pass in any existing SLA policy names that aren't removed
	@Override
	public String assignVmToSla(String vmName, String slaName) {
		List<String> slaNameList = new ArrayList<String>();
		slaNameList = Arrays.asList(slaName.split(","));
		SppAssignment assignData = sppAssignmentService.buildAssignmentDataVm(vmName, slaNameList);
		String assignBody = new Gson().toJson(assignData);
		String postResponse = sppAssignmentService.postAssignmentData(assignBody);
		return postResponse;
	}
	
	// Same as above but for folder
	@Override 
	public String assignFolderToSla(String folderName, String slaName) {
		List<String> slaNameList = new ArrayList<String>();
		slaNameList = Arrays.asList(slaName.split(","));
		SppAssignment assignData = sppAssignmentService.buildAssignmentDataFolder(folderName, slaNameList);
		String assignBody = new Gson().toJson(assignData);
		String postResponse = sppAssignmentService.postAssignmentData(assignBody);
		return postResponse;
	}

}
