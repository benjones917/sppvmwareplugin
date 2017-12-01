package com.ibm.spp.services;

import com.ibm.spp.domain.SppSession;

public interface SppInfoService {

	String getSlaPolicies(SppSession session);

	String getSppVmInfo(String vmName, SppSession session);

	String getSppFolderInfo(String folderName, SppSession session);

}
