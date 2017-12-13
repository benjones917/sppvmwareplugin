package com.ibm.spp.services;

import com.ibm.spp.domain.SppSession;

public interface SppRestoreService {

	String restoreLatestVmTest(String vmName, String vmId, SppSession session);

}
