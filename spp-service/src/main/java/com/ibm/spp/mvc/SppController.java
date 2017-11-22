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

import com.ibm.spp.services.SppService;

@Controller
@RequestMapping(value = "/spp")
public class SppController {
	private final static Log _logger = LogFactory.getLog(SppController.class);
	
	private final SppService _sppService;
	
	@Autowired
	public SppController(
	    @Qualifier("sppService") SppService sppService) {
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

	@ExceptionHandler(Exception.class)
	   @ResponseBody
	   public Map<String, String> handleException(Exception ex, HttpServletResponse response) {
	      response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());

	      Map<String,String> errorMap = new HashMap<String,String>();
	      errorMap.put("message", ex.getMessage());
	      if(ex.getCause() != null) {
	         errorMap.put("cause", ex.getCause().getMessage());
	      }
	      StringWriter sw = new StringWriter();
	      PrintWriter pw = new PrintWriter(sw);
	      ex.printStackTrace(pw);
	      errorMap.put("stackTrace", sw.toString());

	      return errorMap;
	   }
}
