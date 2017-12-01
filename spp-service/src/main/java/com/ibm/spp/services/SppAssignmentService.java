package com.ibm.spp.services;

import com.ibm.spp.domain.SppSession;

public interface SppAssignmentService {

	String assignFolderToSla(String folderName, String slaName, SppSession session);

	String assignVmToSla(String vmName, String slaName, SppSession session);

	

}
