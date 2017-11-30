package com.ibm.spp.services;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Service;

import com.ibm.spp.domain.RegistrationInfo;
import com.ibm.spp.domain.SppSession;

public interface SppRegistrationService {

	void setSppRegistrationInfo(String regInfo);

	SppSession sppLogIn(String host, String user, String pass);

	void sppLogOut(String host, SppSession session);

	RegistrationInfo getSppRegistrationInfo();

}
