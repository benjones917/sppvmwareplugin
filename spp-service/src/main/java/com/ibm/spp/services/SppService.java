package com.ibm.spp.services;

import com.ibm.spp.domain.RegistrationInfo;

public interface SppService {
	String register(String registrationInfo);

	String getSlaPolicies();

	String getSlaPoliciesForVM();

	RegistrationInfo getSppRegistrationInfo();
}
