package com.ibm.spp.services;

import com.ibm.spp.domain.SppSession;

public interface SppInfoService {

	String getSlaPolicies(SppSession session);

	String getSppVmInfo(String vmName, SppSession session);

	String getSppFolderInfo(String folderName, SppSession session);

	String getSppVmInfoForRestore(String vmName, SppSession session);

	String getSppActiveRestoreSessions(SppSession session);

	String getDashboardInfo(SppSession session);
	
	String getSppVmVersions(SppSession session, String vmid, String hvid);
	
	String getSppFolderVersions(SppSession session, String folderid, String hvid);
}
