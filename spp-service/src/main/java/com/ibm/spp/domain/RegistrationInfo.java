package com.ibm.spp.domain;

public class RegistrationInfo {
	public String sppUser;
	public String sppPass;
	public String sppHost;
	
	public RegistrationInfo() {
		
	}
	
	public RegistrationInfo(String sppUser, String sppPass, String sppHost) {
		super();
		this.sppUser = sppUser;
		this.sppPass = sppPass;
		this.sppHost = sppHost;
	}

	public String getSppUser() {
		return sppUser;
	}

	public void setSppUser(String sppUser) {
		this.sppUser = sppUser;
	}

	public String getSppPass() {
		return sppPass;
	}

	public void setSppPass(String sppPass) {
		this.sppPass = sppPass;
	}

	public String getSppHost() {
		return sppHost;
	}

	public void setSppHost(String sppHost) {
		this.sppHost = sppHost;
	}
}
