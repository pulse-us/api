package gov.ca.emsa.pulse.service.controller;

import javax.xml.bind.JAXBException;
import javax.xml.soap.SOAPException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.PRPAIN201310UV02;
import org.opensaml.common.SAMLException;
import org.opensaml.xml.io.MarshallingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import gov.ca.emsa.pulse.common.soap.JSONToSOAPService;
import gov.ca.emsa.pulse.service.EHealthQueryConsumerService;

@RestController
public class PatientDiscoveryController {
	private static final Logger logger = LogManager.getLogger(PatientDiscoveryController.class);
	
	@Autowired EHealthQueryConsumerService consumerService;
	@Autowired JSONToSOAPService jsonConverter;

	@RequestMapping(value = "/patientDiscovery", method = RequestMethod.POST, produces = MediaType.APPLICATION_XML_VALUE)
	public String patientDiscovery(@RequestBody String request)
		throws SAMLException, SOAPException {
		PRPAIN201305UV02 soapRequest = consumerService.unMarshallPatientDiscoveryRequestObject(request);
		PRPAIN201306UV02 responseObj = jsonConverter.createNoPatientRecordsResponse(soapRequest);		
		String response = null;
		try {
			response = consumerService.marshallPatientDiscoveryResponse(responseObj);
		} catch (JAXBException | MarshallingException | SAMLException e) {
			e.printStackTrace();
		}
		logger.info("Patient discovery Response string: " + response);
		return response;
	}
}
