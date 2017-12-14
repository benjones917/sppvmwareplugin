package com.ibm.spp.domain;

import java.util.Map;

import com.google.gson.annotations.Expose;

public class SppVcenterRegistration {
	
	@Expose
	public String hostAddress;
	@Expose
	public int portNumber;
	@Expose
	public String username;
	@Expose
	public String password;
	@Expose
	public boolean sslConnection;
	@Expose
	public Map<String,Integer> opProperties;
	@Expose
	public final String type = "vmware";
	
	public SppVcenterRegistration() {}
	
	public SppVcenterRegistration(String hostAddress, int portNumber, String username, String password,
			boolean sslConnection, Map<String, Integer> opProperties) {
		super();
		this.hostAddress = hostAddress;
		this.portNumber = portNumber;
		this.username = username;
		this.password = password;
		this.sslConnection = sslConnection;
		this.opProperties = opProperties;
	}
	
	public String getHostAddress() {
		return hostAddress;
	}
	public void setHostAddress(String hostAddress) {
		this.hostAddress = hostAddress;
	}
	public int getPortNumber() {
		return portNumber;
	}
	public void setPortNumber(int portNumber) {
		this.portNumber = portNumber;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public boolean isSslConnection() {
		return sslConnection;
	}
	public void setSslConnection(boolean sslConnection) {
		this.sslConnection = sslConnection;
	}
	public Map<String, Integer> getOpProperties() {
		return opProperties;
	}
	public void setOpProperties(Map<String, Integer> opProperties) {
		this.opProperties = opProperties;
	}
	public String getType() {
		return type;
	}

}
