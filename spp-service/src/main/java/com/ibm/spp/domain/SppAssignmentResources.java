package com.ibm.spp.domain;

import com.google.gson.annotations.Expose;

public class SppAssignmentResources {
	
	// may need name for easier comparison but don't want to expose for GSON serialization
	// name is not needed for assignment API call to SPP
	public String name;
	@Expose
	public String href;
	@Expose
	public String id;
	@Expose
	public String metadataPath;
	
	public SppAssignmentResources(String href, String id, String metadataPath) {
		super();
		this.href = href;
		this.id = id;
		this.metadataPath = metadataPath;
	}
	
	public SppAssignmentResources() {
		
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMetadataPath() {
		return metadataPath;
	}

	public void setMetadataPath(String metadataPath) {
		this.metadataPath = metadataPath;
	}
	
	

}
