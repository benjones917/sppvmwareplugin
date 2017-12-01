package com.ibm.spp.domain;

import com.google.gson.annotations.Expose;

public class SppRestoreVersion {

	@Expose
	public String href;
	@Expose
	public SppRestoreVersionMetadata metadata;
	
	public SppRestoreVersion() {
		
	}
	
	public SppRestoreVersion(String href, SppRestoreVersionMetadata metadata) {
		super();
		this.href = href;
		this.metadata = metadata;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public SppRestoreVersionMetadata getMetadata() {
		return metadata;
	}

	public void setMetadata(SppRestoreVersionMetadata metadata) {
		this.metadata = metadata;
	}
	
	

}
