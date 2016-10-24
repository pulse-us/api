package gov.ca.emsa.pulse.service;

import gov.ca.emsa.pulse.broker.manager.DocumentManager;
import gov.ca.emsa.pulse.broker.manager.PatientManager;
import gov.ca.emsa.pulse.broker.saml.SamlGenerator;
import io.swagger.annotations.Api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "documents")
@RestController
@RequestMapping("/documents")
public class DocumentService {
	@Autowired DocumentManager docManager;
	@Autowired PatientManager patientManager;
	@Autowired SamlGenerator samlGenerator;
		
		
}