package com.ibm.spp.services;

public interface SppInfoService {

	String getSlaPolicies();

	String getSppVmInfo(String vmName);

	String getSppFolderInfo(String folderName);

}
