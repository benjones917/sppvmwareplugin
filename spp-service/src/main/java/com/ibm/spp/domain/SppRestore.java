package com.ibm.spp.domain;

import com.google.gson.annotations.Expose;

public class SppRestore {

	@Expose
	public final String subType = "vmware";
	@Expose
	public SppRestoreSpec spec;
	
	public SppRestore() {
		
	}
	
	public SppRestore(SppRestoreSpec spec) {
		super();
		this.spec = spec;
	}

	public SppRestoreSpec getSpec() {
		return spec;
	}

	public void setSpec(SppRestoreSpec spec) {
		this.spec = spec;
	}

	public String getSubType() {
		return subType;
	}
	
	

}
