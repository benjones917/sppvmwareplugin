package com.ibm.spp.domain;

import com.google.gson.annotations.Expose;

public class SppRestoreVersionMetadata {
	
	@Expose
	public boolean useLatest;
	@Expose
	public String protectionTime;
	@Expose
	public String name;
	
	public SppRestoreVersionMetadata() {
		super();
	}

	public boolean isUseLatest() {
		return useLatest;
	}

	public String getProtectionTime() {
		return protectionTime;
	}

	public void setProtectionTime(String protectionTime) {
		this.protectionTime = protectionTime;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setUseLatest(boolean useLatest) {
		this.useLatest = useLatest;
	}

}
