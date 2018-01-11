package com.ibm.spp.services;

import com.ibm.spp.domain.RegistrationInfo;
import com.ibm.spp.domain.SppSession;

public interface SppRegistrationService {

	void setSppRegistrationInfo(String regInfo);

	SppSession sppLogIn(RegistrationInfo regInfo);

	void sppLogOut(SppSession session);

	RegistrationInfo getSppRegistrationInfo();
	
	String registerVcenter(String vcRegInfo, SppSession session);
	
	String getVcRegistration(String vcId, SppSession session);

}
