package com.ibm.spp.domain;

public final class SppUrls {
	public static final String sppSessionUrl = "/core/api/endeavour/session";
	public static final String sppSlaUrl = "/spp/ecxngp/slapolicy";
	public static final String sppVmUrl = "/core/api/hypervisor/search?resourceType=vm&from=hlo";
	public static final String sppAssignUrl = "/spp/ecxngp/hypervisor?action=applySLAPolicies";
	public static final String sppFolderUrl = "/core/api/hypervisor/search?resourceType=folder&from=hlo";
	public static final String sppVmRestoreUrl = "/core/api/hypervisor/search?resourceType=vm&from=restore";
	public static final String sppVmRestoreActionUrl = "/spp/ecxngp/hypervisor?action=restore";
}
