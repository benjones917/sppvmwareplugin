package com.ibm.spp.services;

import com.ibm.spp.domain.RegistrationInfo;

public interface SppService {
	String register(String registrationInfo);

	String getSlaPolicies();

	RegistrationInfo getSppRegistrationInfo();

	String getSppVmInfo(String vmName);
}
