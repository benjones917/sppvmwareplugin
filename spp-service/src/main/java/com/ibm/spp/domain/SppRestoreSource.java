package com.ibm.spp.domain;

import java.util.Map;

import com.google.gson.annotations.Expose;

public class SppRestoreSource {
	
	@Expose
	public final Boolean include = true;
	@Expose
	public final String resourceType = "vm";
	@Expose
	public String href;
	@Expose
	public Map<String,String> metadata;
	@Expose
	public String id;
	@Expose
	public SppRestoreVersion version;
	
	public SppRestoreSource() {
		
	}
	
	public SppRestoreSource(String href, Map<String, String> metadata, String id, SppRestoreVersion version) {
		super();
		this.href = href;
		this.metadata = metadata;
		this.id = id;
		this.version = version;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public Map<String, String> getMetadata() {
		return metadata;
	}

	public void setMetadata(Map<String, String> metadata) {
		this.metadata = metadata;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public SppRestoreVersion getVersion() {
		return version;
	}

	public void setVersion(SppRestoreVersion version) {
		this.version = version;
	}

	public Boolean getInclude() {
		return include;
	}

	public String getResourceType() {
		return resourceType;
	}

}
