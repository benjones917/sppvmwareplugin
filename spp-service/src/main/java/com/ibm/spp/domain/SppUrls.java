package com.ibm.spp.domain;

public final class SppUrls {
	public static final String sppSessionUrl = "/api/endeavour/session";
	public static final String sppSlaUrl = "/ngp/slapolicy";
	public static final String sppVmUrl = "/api/hypervisor/search?resourceType=vm&from=hlo";
	public static final String sppAssignUrl = "/ngp/hypervisor?action=applySLAPolicies";
	public static final String sppFolderUrl = "/api/hypervisor/search?resourceType=folder&from=hlo";
	public static final String sppVmRestoreUrl = "/api/hypervisor/search?resourceType=vm&from=restore";
	public static final String sppVmRestoreActionUrl = "/ngp/hypervisor?action=restore";
	public static final String sppJobSessionUrl = "/api/endeavour/jobsession?pageSize=100&sort=%5B%7B%22property%22:%22start%22,%22direction%22:%22DESC%22%7D%5D&filter=%5B%7B%22property%22:%22serviceId%22,%22value%22:%5B%22com.catalogic.ecx.serviceprovider.recovery.hypervisor%22%5D,%22op%22:%22IN%22%7D,%7B%22property%22:%22status%22,%22value%22:%22PENDING%22,%22op%22:%22=%22%7D,%7B%22property%22:%22subType%22,%22value%22:%22vmware%22,%22op%22:%22=%22%7D%5D";
	public static final String sppVmVersionUrl = "/api/hypervisor/HVID/vm/VMID/version?from=recovery";
	public static final String sppFolderVersionUrl = "/api/hypervisor/HVID/folder/FOLDERID/version?from=recovery";
	public static final String sppHypervisorUrl = "/api/hypervisor";
	public static final String sppRegisterHypervisorUrl = "/ngp/hypervisor";
	public static final String sppAllVmsUrl = "/api/hypervisor/HVID/vm?from=hlo";
}
