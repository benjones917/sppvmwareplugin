package com.ibm.spp.services;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.google.gson.Gson;
import com.ibm.spp.domain.RegistrationInfo;
import com.ibm.spp.domain.SppSession;

public class SppServiceImpl implements SppService {
	
	private static final Log log = LogFactory.getLog(SppServiceImpl.class);
	
	SppRegistrationService sppRegistrationService = new SppRegistrationServiceImpl();
	SppInfoService sppInfoService = new SppInfoServiceImpl();
	SppAssignmentService sppAssignmentService = new SppAssignmentServiceImpl();
	SppRestoreService sppRestoreService = new SppRestoreServiceImpl();
	
	// Main function to register SPP
	// Saving of registration is dependent on successful login
	// Currently returns String response of status
	@Override
	public String register(String registrationInfo) {
		RegistrationInfo regInfo = new Gson().fromJson(registrationInfo, RegistrationInfo.class);
		SppSession session = sppRegistrationService.sppLogIn(regInfo);
		if(session.getSessionId() != null) {
			deleteSppSession(session);
			sppRegistrationService.setSppRegistrationInfo(registrationInfo);
			log.info("Registration complete");
			return "Registration succesful";
		} else {
			log.info("Registration failed");
			return "Registration failed";
		}
	}

	@Override
	public String getSlaPolicies() {
		SppSession session = createSppSession();
		return sppInfoService.getSlaPolicies(session);
	}
	
	@Override 
	public String getSppVmInfo(String vmName) {
		SppSession session = createSppSession();
		String vmInfo = sppInfoService.getSppVmInfo(vmName, session);
		deleteSppSession(session);
		return vmInfo;
	}

	@Override
	public String getSppFolderInfo(String folderName) {
		SppSession session = createSppSession();
		String folderInfo = sppInfoService.getSppFolderInfo(folderName, session);
		deleteSppSession(session);
		return folderInfo;
	}

	@Override
	public RegistrationInfo getSppRegistrationInfo() {
		return sppRegistrationService.getSppRegistrationInfo();
	}

	@Override
	public String assignVmToSla(String vmName, String slaName) {
		SppSession session = createSppSession();
		String assignVm = sppAssignmentService.assignVmToSla(vmName, slaName, session);
		deleteSppSession(session);
		return assignVm;
	}

	@Override
	public String assignFolderToSla(String folderName, String slaName) {
		SppSession session = createSppSession();
		String assignFolder = sppAssignmentService.assignFolderToSla(folderName, slaName, session);
		deleteSppSession(session);
		return assignFolder;
	}
	
	@Override
	public String restoreLatestVmTest(String vmName) {
		SppSession session = createSppSession();
		String restoreVm = sppRestoreService.restoreLatestVmTest(vmName, session);
		deleteSppSession(session);
		return restoreVm;
	}
	
	private SppSession createSppSession() {
		RegistrationInfo regInfo = getSppRegistrationInfo();
		return sppRegistrationService.sppLogIn(regInfo);
	}
	
	private void deleteSppSession(SppSession session) {
		sppRegistrationService.sppLogOut(session);
	}

}
