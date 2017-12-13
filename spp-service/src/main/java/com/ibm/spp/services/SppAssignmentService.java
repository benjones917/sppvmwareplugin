package com.ibm.spp.services;

import com.ibm.spp.domain.SppSession;

public interface SppAssignmentService {

	String assignFolderToSla(String folderName, String groupId, String slaName, SppSession session);

	String assignVmToSla(String vmName, String vmId, String slaName, SppSession session);

	

}
