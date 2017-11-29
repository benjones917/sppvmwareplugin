package com.ibm.spp.services;

import java.util.List;

import com.ibm.spp.domain.RegistrationInfo;

public interface SppService {
	String register(String registrationInfo);

	String getSlaPolicies();

	RegistrationInfo getSppRegistrationInfo();

	String getSppVmInfo(String vmName);

	String assignVmToSla(String vmName, List<String> slaName);

	String assignFolderToSla(String folderName, List<String> slaName);
}
