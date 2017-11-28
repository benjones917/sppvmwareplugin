package com.ibm.spp.domain;

public class SppAssignmentSlapols {
	
	public String href;
	public String id;
	public String name;
	
	public SppAssignmentSlapols() {
		
	}

	public SppAssignmentSlapols(String href, String id, String name) {
		super();
		this.href = href;
		this.id = id;
		this.name = name;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}
