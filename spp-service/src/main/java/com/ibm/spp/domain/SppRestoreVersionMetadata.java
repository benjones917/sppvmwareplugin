package com.ibm.spp.domain;

import com.google.gson.annotations.Expose;

public class SppRestoreVersionMetadata {
	
	@Expose
	public final boolean useLatest = true;
	@Expose
	public final String name = "Use Latest";
	
	public SppRestoreVersionMetadata() {
		super();
	}

	public boolean isUseLatest() {
		return useLatest;
	}

	public String getName() {
		return name;
	}
	
	
	
	
}
