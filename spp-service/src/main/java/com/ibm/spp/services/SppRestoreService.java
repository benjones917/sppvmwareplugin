package com.ibm.spp.services;

import com.ibm.spp.domain.SppSession;

public interface SppRestoreService {

	String restoreLatestVmTest(String vmName, String vmId, SppSession session);

	String restoreLatestVmProd(String vmName, String vmId, SppSession session);

	String restoreVersionVmTest(String vmName, String vmId, String version, SppSession session);

	String restoreVersionVmProd(String vmName, String vmId, String version, SppSession session);

}
