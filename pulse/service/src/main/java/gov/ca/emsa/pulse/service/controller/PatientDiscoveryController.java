package gov.ca.emsa.pulse.service.controller;

import java.io.IOException;

import javax.xml.soap.SOAPException;

import gov.ca.emsa.pulse.common.domain.PatientSearch;
import gov.ca.emsa.pulse.service.EHealthQueryConsumerService;
import gov.ca.emsa.pulse.service.SOAPToJSONService;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.hl7.v3.PRPAIN201305UV02;
import org.opensaml.common.SAMLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

@RestController
public class PatientDiscoveryController {
	private static final Logger logger = LogManager.getLogger(PatientDiscoveryController.class);
	@Autowired private ResourceLoader resourceLoader;
	private static final String RESOURCE_FILE_NAME = "ValidXcpdResponse.xml";
	
	@Autowired EHealthQueryConsumerService consumerService;
	@Autowired SOAPToJSONService converter;
	
	@RequestMapping(value = "/patientDiscovery", method = RequestMethod.POST, produces={"application/xml"} , consumes ={"application/xml"})
	public String patientDiscovery(@RequestBody String request){
		PRPAIN201305UV02 requestObj = null;
		try{
			requestObj = consumerService.unMarshallPatientDiscoveryRequestObject(request);
		}catch(SAMLException e){
			return consumerService.createSOAPFault();
		} catch (SOAPException e) {
			logger.error(e);
		}
		PatientSearch patientSearch = converter.convertToPatientSearch(requestObj);
		try {
			Resource documentsFile = resourceLoader.getResource("classpath:" + RESOURCE_FILE_NAME);
			return Resources.toString(documentsFile.getURL(), Charsets.UTF_8);
		} catch (IOException e) {
			logger.error(e);
			throw new HttpMessageNotWritableException("Could not read response file");
		}
	}
}
