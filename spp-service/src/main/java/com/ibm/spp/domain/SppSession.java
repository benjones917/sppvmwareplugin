package com.ibm.spp.domain;

public class SppSession {
	public String sessionid;
	
	public SppSession() {
		
	}
	
	public SppSession(String sessionid) {
		super();
		this.sessionid = sessionid;
	}

	public String getSessionId() {
		return sessionid;
	}

	public void setSessionId(String sessionId) {
		this.sessionid = sessionId;
	}
	
	
}
