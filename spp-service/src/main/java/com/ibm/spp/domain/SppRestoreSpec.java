package com.ibm.spp.domain;

import java.util.List;

import com.google.gson.annotations.Expose;

public class SppRestoreSpec {

	@Expose
	public List<SppRestoreSource> source;
	@Expose
	public List<SppRestoreSubpolicy> subpolicy;
	
	public SppRestoreSpec() {
		
	}
	
	public SppRestoreSpec(List<SppRestoreSource> source, List<SppRestoreSubpolicy> subpolicy) {
		super();
		this.source = source;
		this.subpolicy = subpolicy;
	}

	public List<SppRestoreSource> getSource() {
		return source;
	}

	public void setSource(List<SppRestoreSource> source) {
		this.source = source;
	}

	public List<SppRestoreSubpolicy> getSubpolicy() {
		return subpolicy;
	}

	public void setSubpolicy(List<SppRestoreSubpolicy> subpolicy) {
		this.subpolicy = subpolicy;
	}
	
}
