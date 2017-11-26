package com.ibm.spp.services;

public interface SppService {
	String register(String registrationInfo);

	String getSlaPolicies();

	String getSlaPoliciesForVM();
}
