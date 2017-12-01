package com.ibm.spp.services;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gson.JsonObject;
import com.ibm.spp.domain.SppSession;

public class SppRestoreServiceImpl implements SppRestoreService {
	
	private static final Log log = LogFactory.getLog(SppServiceImpl.class);
	
	SppInfoService sppInfoService = new SppInfoServiceImpl();
	
	@Override
	public String restoreLatestVmTest(String vmName, SppSession session) {
		JsonObject postJsonData = buildRestoreDataLatestTest(vmName, session);
		String postResponse = postRestoreData(postJsonData, session);
		return postResponse;
	}
	
	private String postRestoreData(JsonObject postJsonData, SppSession session) {
		return null;
	}

	private JsonObject buildRestoreDataLatestTest(String vmName, SppSession session) {
		String vmData = sppInfoService.getSppVmInfo(vmName, session);
		JsonObject body = new JsonObject();
		body.addProperty("subType", "vmware");
		// can just build json here but probably should use Java objects
		return body;
	}

}
