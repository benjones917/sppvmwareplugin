package com.ibm.spp.domain;

import java.util.List;

public class SppAssignment {
	
	public static final String subtype = "vmware";
	public static final String version = "1.0";
	
	public List<SppAssignmentResources> resources;
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

	public static String getSubtype() {
		return subtype;
	}

	public static String getVersion() {
		return version;
	}
	
	

}
