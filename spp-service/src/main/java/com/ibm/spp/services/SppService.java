package com.ibm.spp.services;

import com.ibm.spp.domain.RegistrationInfo;

public interface SppService {
	String register(String registrationInfo);

	String getSlaPolicies();

	RegistrationInfo getSppRegistrationInfo();

	String getSppVmInfo(String vmName, String vmId);

	String assignVmToSla(String vmName, String vmId, String slaName);

	String assignFolderToSla(String folderName, String groupId, String slaName);

	String getSppFolderInfo(String folderName, String groupId);

	String restoreLatestVmTest(String vmName, String vmId);

	String getSppActiveRestoreSessions();
	
	String getSppVmVersionInfo(String vmid, String hvid);
	
	String getSppFolderVersionInfo(String vmid, String hvid);
	
	String registerVcenter(String registrationInfo);
	
	String getVcRegistration(String vcId);

	String getDashboardInfo(String vcId);
}
