package com.ibm.spp.domain;

public final class SppUrls {
	public static final String sppSessionUrl = "/core/api/endeavour/session";
	public static final String sppSlaUrl = "/spp/ecxngp/slapolicy";
	public static final String sppVmUrl = "/core/api/hypervisor/search?resourceType=vm&from=hlo";
	public static final String sppAssignUrl = "/spp/ecxngp/hypervisor?action=applySLAPolicies";
	public static final String sppFolderUrl = "/core/api/hypervisor/search?resourceType=folder&from=hlo";
	public static final String sppVmRestoreUrl = "/core/api/hypervisor/search?resourceType=vm&from=restore";
	public static final String sppVmRestoreActionUrl = "/spp/ecxngp/hypervisor?action=restore";
	public static final String sppJobSessionUrl = "https://172.20.49.50/core/api/endeavour/jobsession?pageSize=100&sort=%5B%7B%22property%22:%22start%22,%22direction%22:%22DESC%22%7D%5D&filter=%5B%7B%22property%22:%22serviceId%22,%22value%22:%5B%22com.catalogic.ecx.serviceprovider.recovery.hypervisor%22%5D,%22op%22:%22IN%22%7D,%7B%22property%22:%22status%22,%22value%22:%22PENDING%22,%22op%22:%22=%22%7D,%7B%22property%22:%22subType%22,%22value%22:%22vmware%22,%22op%22:%22=%22%7D%5D";
}
