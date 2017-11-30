package com.ibm.spp.services;

import java.util.List;

import com.ibm.spp.domain.SppAssignment;
import com.ibm.spp.domain.SppAssignmentResources;

public interface SppAssignmentService {

	String postAssignmentData(String assignBody);

	SppAssignment buildAssignmentDataVm(String vmName, List<String> slaNameList);

	SppAssignment buildAssignmentDataFolder(String folderName, List<String> slaNameList);

}
