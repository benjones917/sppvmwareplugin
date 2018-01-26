package com.ibm.spp.domain;

import com.google.gson.annotations.Expose;

public class SppRestoreOption {

	@Expose
	public final String protocolpriority = "iSCSI";
	@Expose
	public final boolean poweron = false;
	@Expose
	public final boolean continueonerror = true;
	@Expose
	public final boolean autocleanup = true;
	@Expose
	public final boolean allowsessoverwrite = true;
	@Expose
	public String mode;
	@Expose
	public final boolean vmscripts = false;
	@Expose
	public boolean allowvmoverwrite;
	
	public SppRestoreOption() {
		super();
	}

	public String getProtocolpriority() {
		return protocolpriority;
	}

	public boolean isPoweron() {
		return poweron;
	}

	public boolean isContinueonerror() {
		return continueonerror;
	}

	public boolean isAutocleanup() {
		return autocleanup;
	}

	public boolean isAllowsessoverwrite() {
		return allowsessoverwrite;
	}

	public String getMode() {
		return mode;
	}

	public boolean isVmscripts() {
		return vmscripts;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public boolean isAllowvmoverwrite() {
		return allowvmoverwrite;
	}

	public void setAllowvmoverwrite(boolean allowvmoverwrite) {
		this.allowvmoverwrite = allowvmoverwrite;
	}
	
	
}
