package com.ibm.spp.domain;

public class SppAssignmentResources {
	
	public String href;
	public String id;
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
