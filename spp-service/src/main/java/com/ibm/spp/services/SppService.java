package com.ibm.spp.services;

import com.ibm.spp.domain.RegistrationInfo;

public interface SppService {
	String register(String registrationInfo);

	String getSlaPolicies();

	RegistrationInfo getSppRegistrationInfo();

	String getSppVmInfo(String vmName);

	String assignVmToSla(String vmName, String slaName);

	String assignFolderToSla(String folderName, String slaName);

	String getSppFolderInfo(String folderName);

	String restoreLatestVmTest(String vmName);
}
