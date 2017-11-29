package com.ibm.spp.domain;

import java.util.List;

import com.google.gson.annotations.Expose;

public class SppAssignment {
	
	@Expose
	public final String subtype = "vmware";
	@Expose
	public final String version = "1.0";
	
	@Expose
	public List<SppAssignmentResources> resources;
	@Expose
	public List<SppAssignmentSlapols> slapolicies;
	
	public SppAssignment() {
		
	}
	
	public SppAssignment(List<SppAssignmentResources> resources, List<SppAssignmentSlapols> slapolicies) {
		super();
		this.resources = resources;
		this.slapolicies = slapolicies;
	}

	public List<SppAssignmentResources> getResources() {
		return resources;
	}

	public void setResources(List<SppAssignmentResources> resources) {
		this.resources = resources;
	}

	public List<SppAssignmentSlapols> getSlapolicies() {
		return slapolicies;
	}

	public void setSlapolicies(List<SppAssignmentSlapols> slapolicies) {
		this.slapolicies = slapolicies;
	}

	public String getSubtype() {
		return subtype;
	}

	public String getVersion() {
		return version;
	}
	
	

}
