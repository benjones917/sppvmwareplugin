package com.ibm.spp.domain;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.annotations.Expose;

public class SppRestoreSubpolicy {

	@Expose
	public String type;
	@Expose
	public final Map<String,Boolean> destination = createDestMap();
	@Expose
	public SppRestoreOption option;
	
	public SppRestoreSubpolicy() {
		
	}
	
	//initializer for currently literal destination field
	private Map<String, Boolean> createDestMap() {
		Map<String,Boolean> destMap = new HashMap<String,Boolean>();
		destMap.put("systemDefined", true);
		return destMap;
	}

	public SppRestoreSubpolicy(String type, SppRestoreOption option) {
		super();
		this.type = type;
		this.option = option;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Map<String, Boolean> getDestination() {
		return destination;
	}

	public SppRestoreOption getOption() {
		return option;
	}

	public void setOption(SppRestoreOption option) {
		this.option = option;
	}
	
	
}
