package com.ibm.spp.mvc;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.ibm.spp.domain.RegistrationInfo;
import com.ibm.spp.services.SppService;

@Controller
@RequestMapping(value = "/spp")
public class SppController {
	private final static Log _logger = LogFactory.getLog(SppController.class);

	private final SppService _sppService;

	@Autowired
	public SppController(@Qualifier("sppService") SppService sppService) {
		_sppService = sppService;
	}

	public SppController() {
		_sppService = null;
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	@ResponseBody
	public String register(@RequestParam(value = "registrationInfo", required = true) String registrationInfo)
			throws Exception {
		return _sppService.register(registrationInfo);
	}

	@RequestMapping(value = "/register", method = RequestMethod.GET)
	@ResponseBody
	public String register() throws Exception {
		RegistrationInfo regInfo = new RegistrationInfo();
		regInfo = _sppService.getSppRegistrationInfo();
		String registration = new Gson().toJson(regInfo);
		return registration;
	}
	
	@RequestMapping(value = "/vcreg", method = RequestMethod.POST)
	@ResponseBody
	public String vcReg(@RequestParam(value = "registrationInfo", required = true) String registrationInfo)
			throws Exception {
		return _sppService.registerVcenter(registrationInfo);
	}

	@RequestMapping(value = "/vcreg", method = RequestMethod.GET)
	@ResponseBody
	public String getVcReg(@RequestParam(value = "vcid", required = true) String vcId) 
			throws Exception {
		String registration = _sppService.getVcRegistration(vcId);
		return registration;
	}

	@RequestMapping(value = "/sla", method = RequestMethod.GET)
	@ResponseBody
	public String getAllSlaPols() throws Exception {
		String slaPols = _sppService.getSlaPolicies();
		return slaPols;
	}

	@RequestMapping(value = "/vm", method = RequestMethod.GET)
	@ResponseBody
	public String getSppVmInfo(@RequestParam(value = "vm", required = true) String vm,
			@RequestParam(value = "vmid", required = true) String vmid) throws Exception {
		String vmInfo = _sppService.getSppVmInfo(vm, vmid);
		return vmInfo;
	}

	@RequestMapping(value = "/folder", method = RequestMethod.GET)
	@ResponseBody
	public String getSppFolderInfo(@RequestParam(value = "folder", required = true) String folder,
			@RequestParam(value = "groupid", required = true) String groupid) throws Exception {
		String folderInfo = _sppService.getSppFolderInfo(folder, groupid);
		return folderInfo;
	}

	// sla String parameter represents a list of SLA policies
	@RequestMapping(value = "/assignvm", method = RequestMethod.POST)
	@ResponseBody
	public String assignVmToSla(@RequestParam(value = "vm", required = true) String vm,
			@RequestParam(value = "sla", required = true) String sla,
			@RequestParam(value = "vmid", required = true) String vmid) throws Exception {
		String vmAssignment = _sppService.assignVmToSla(vm, vmid, sla);
		return vmAssignment;
	}

	// sla String parameter represents a list of SLA policies
	@RequestMapping(value = "/assignfolder", method = RequestMethod.POST)
	@ResponseBody
	public String assignFolderToSla(@RequestParam(value = "folder", required = true) String folder, 
			@RequestParam(value = "groupid", required = true) String groupid,
			@RequestParam(value = "sla", required = true) String sla) throws Exception {
		String folderAssignment = _sppService.assignFolderToSla(folder, groupid, sla);
		return folderAssignment;
	}

	// restore latest VM as in test mode
	// for now this is the only option for restore
	// will add restore version selection in the future
	@RequestMapping(value = "/restore/latest/test", method = RequestMethod.POST)
	@ResponseBody
	public String restoreLatestVmTest(@RequestParam(value = "vm", required = true) String vm,
			@RequestParam(value = "vmid", required = true) String vmid) 
			throws Exception {
		String vmRestore = _sppService.restoreLatestVmTest(vm, vmid);
		return vmRestore;
	}
	
	// get active restore sessions from SPP
	@RequestMapping(value = "/activerestores", method = RequestMethod.GET)
	@ResponseBody
	public String getActiveRestores() throws Exception {
		String restoreSessions = _sppService.getSppActiveRestoreSessions();
		return restoreSessions;
	}
	
	@RequestMapping(value = "/vmversions", method = RequestMethod.GET)
	@ResponseBody
	public String getSppVmVersionInfo(@RequestParam(value = "vmid", required = true) String vmid,
			@RequestParam(value = "hvid", required = true) String hvid) throws Exception {
		String vmVersionInfo = _sppService.getSppVmVersionInfo(vmid, hvid);
		return vmVersionInfo;
	}
	
	@RequestMapping(value = "/folderversions", method = RequestMethod.GET)
	@ResponseBody
	public String getSppFolderVersionInfo(@RequestParam(value = "folderid", required = true) String folderid,
			@RequestParam(value = "hvid", required = true) String hvid) throws Exception {
		String vmVersionInfo = _sppService.getSppFolderVersionInfo(folderid, hvid);
		return vmVersionInfo;
	}
	
	@RequestMapping(value = "/dashboard", method = RequestMethod.GET)
	@ResponseBody
	public String getSppDashInfo(@RequestParam(value = "hvid", required = true) String hvid) throws Exception {
		String dashInfo = _sppService.getDashboardInfo(hvid);
		return dashInfo;
	}

	@ExceptionHandler(Exception.class)
	@ResponseBody
	public Map<String, String> handleException(Exception ex, HttpServletResponse response) {
		response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());

		Map<String, String> errorMap = new HashMap<String, String>();
		errorMap.put("message", ex.getMessage());
		if (ex.getCause() != null) {
			errorMap.put("cause", ex.getCause().getMessage());
		}
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		ex.printStackTrace(pw);
		errorMap.put("stackTrace", sw.toString());

		return errorMap;
	}
}
