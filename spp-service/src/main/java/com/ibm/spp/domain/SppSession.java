package com.ibm.spp.domain;

public class SppSession {
	public String sessionid;
	public transient String host;
	public transient String user;
	
	public SppSession() {
		
	}
	
	public SppSession(String sessionid, String host, String user) {
		super();
		this.sessionid = sessionid;
		this.host = host;
		this.user = user;
	}

	public String getSessionId() {
		return sessionid;
	}

	public void setSessionId(String sessionId) {
		this.sessionid = sessionId;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}
	
	
}
