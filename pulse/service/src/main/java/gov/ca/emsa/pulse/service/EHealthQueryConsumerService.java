package gov.ca.emsa.pulse.service;

import gov.ca.emsa.pulse.xcpd.PatientDiscoveryResponse;
import gov.ca.emsa.pulse.xcpd.XcpdUtils;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.http.MediaType;

@RestController
public class EHealthQueryConsumerService {
	
	@RequestMapping(value = "/patientDiscovery", method = RequestMethod.GET, produces = MediaType.APPLICATION_XML_VALUE)
	public PatientDiscoveryResponse patientDiscovery(){
		PatientDiscoveryResponse pdr = XcpdUtils.generateQueryResponse();
		return pdr;
	}

}
