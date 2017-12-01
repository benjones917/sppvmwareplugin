package com.ibm.spp.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ibm.spp.domain.SppRestore;
import com.ibm.spp.domain.SppRestoreOption;
import com.ibm.spp.domain.SppRestoreSource;
import com.ibm.spp.domain.SppRestoreSpec;
import com.ibm.spp.domain.SppRestoreSubpolicy;
import com.ibm.spp.domain.SppRestoreVersion;
import com.ibm.spp.domain.SppRestoreVersionMetadata;
import com.ibm.spp.domain.SppSession;

public class SppRestoreServiceImpl implements SppRestoreService {
	
	private static final Log log = LogFactory.getLog(SppServiceImpl.class);
	
	SppInfoService sppInfoService = new SppInfoServiceImpl();
	
	@Override
	public String restoreLatestVmTest(String vmName, SppSession session) {
		String postJsonData = buildRestoreDataLatestTest(vmName, session);
		String postResponse = postRestoreData(postJsonData, session);
		return postResponse;
	}
	
	private String postRestoreData(String postJsonData, SppSession session) {
		return postJsonData;
	}

	// main function to build restore JSON data for post
	// can eventually be broken out into separate functions
	// for each obj as we add more restore features/options
	private String buildRestoreDataLatestTest(String vmName, SppSession session) {
		// not getting proper format in href data with this call
		// may need to make a different call
		// could also be different API to determine what VMs do/don't have restores available
		String vmData = sppInfoService.getSppVmInfo(vmName, session);
		JsonParser parser = new JsonParser();
		JsonObject vmDataJson = parser.parse(vmData).getAsJsonObject();
		
		SppRestore sppRestore = new SppRestore();
		SppRestoreSpec sppRestoreSpec = new SppRestoreSpec();
		SppRestoreSource sppRestoreSource = new SppRestoreSource();
		SppRestoreOption sppRestoreOption = new SppRestoreOption();
		SppRestoreSubpolicy sppRestoreSubpolicy = new SppRestoreSubpolicy();
		SppRestoreVersion sppRestoreVersion = new SppRestoreVersion();
		SppRestoreVersionMetadata sppRestoreVersionMetadata = new SppRestoreVersionMetadata();
		
		List<SppRestoreSource> sppRestoreSourceList = new ArrayList<SppRestoreSource>();
		List<SppRestoreSubpolicy> sppRestoreSubpolicyList = new ArrayList<SppRestoreSubpolicy>();
		
		sppRestoreVersion.setMetadata(sppRestoreVersionMetadata);
		sppRestoreVersion.setHref("<GET HREF>");
		
		sppRestoreSource.setVersion(sppRestoreVersion);
		sppRestoreSource.setHref("<GET HREF>");
		Map<String,String> mdMap = new HashMap<String,String>();
		mdMap.put("name", "<GET NAME>");
		sppRestoreSource.setMetadata(mdMap);
		sppRestoreSource.setId("<GET ID>");
		
		sppRestoreSubpolicy.setType("IV");
		sppRestoreSubpolicy.setOption(sppRestoreOption);
		
		sppRestoreSourceList.add(sppRestoreSource);
		sppRestoreSubpolicyList.add(sppRestoreSubpolicy);
		
		sppRestoreSpec.setSource(sppRestoreSourceList);
		sppRestoreSpec.setSubpolicy(sppRestoreSubpolicyList);
		sppRestore.setSpec(sppRestoreSpec);
		
		String body = new Gson().toJson(sppRestore);
		return body;
	}

}
