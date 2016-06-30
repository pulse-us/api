package gov.ca.emsa.pulse.service;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.opensaml.xml.io.MarshallingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import gov.ca.emsa.pulse.broker.domain.Document;
import gov.ca.emsa.pulse.broker.domain.Patient;
import gov.ca.emsa.pulse.broker.domain.User;
import gov.ca.emsa.pulse.broker.dto.AlternateCareFacilityDTO;
import gov.ca.emsa.pulse.broker.dto.DocumentDTO;
import gov.ca.emsa.pulse.broker.dto.PatientDTO;
import gov.ca.emsa.pulse.broker.manager.AlternateCareFacilityManager;
import gov.ca.emsa.pulse.broker.manager.DocumentManager;
import gov.ca.emsa.pulse.broker.manager.PatientManager;
import gov.ca.emsa.pulse.broker.saml.SAMLInput;
import gov.ca.emsa.pulse.broker.saml.SamlGenerator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "patients")
@RestController
@RequestMapping("/patients")
public class PatientService {
	private static final Logger logger = LogManager.getLogger(PatientService.class);
	@Autowired SamlGenerator samlGenerator;
	@Autowired private PatientManager patientManager;
	@Autowired private DocumentManager docManager;
	@Autowired private AlternateCareFacilityManager acfManager;
	
	public PatientService() {
	}
		
	@ApiOperation(value="Get all patients at the logged-in user's ACF")
	@RequestMapping("/")
	public List<Patient> getPatientsAtAcf() throws InvalidParameterException {
		User user = UserUtil.getCurrentUser();
		AlternateCareFacilityDTO acfDto = acfManager.getByName(user.getAcf());
		if(acfDto == null || acfDto.getId() == null) {
			throw new InvalidParameterException("The ACF supplied, '" + user.getAcf() + "' was not found in the database.");
		}
		
		List<PatientDTO> queryResults = patientManager.getPatientsAtAcf(acfDto.getId());
		List<Patient> results = new ArrayList<Patient>(queryResults.size());
        for(PatientDTO patientDto : queryResults) {
        	results.add(new Patient(patientDto));
        }
        
        return results;
	}
	
	@ApiOperation(value="Get a list of documents associated with the given patient")
	@RequestMapping("/{patientId}/documents")
	public List<Document> getDocumentListForPatient(@PathVariable("patientId")Long patientId) {
		List<DocumentDTO> docDtos = docManager.getDocumentsForPatient(patientId);
		List<Document> results = new ArrayList<Document>(docDtos.size());
		for(DocumentDTO docDto : docDtos) {
			results.add(new Document(docDto));
		}
		return results;
	}
	
	@ApiOperation(value="Retrieve a specific document from an organization.")
	@RequestMapping(value = "/{patientId}/documents/{documentId}")
	public @ResponseBody String getDocumentContents(@PathVariable("patientId") Long patientId,
			@PathVariable("documentId") Long documentId,
			@RequestParam(value="cacheOnly", required= false, defaultValue="true") Boolean cacheOnly) {
		
		User user = UserUtil.getCurrentUser();
		SAMLInput input = new SAMLInput();
		input.setStrIssuer("https://idp.dhv.gov");
		input.setStrNameID(user.getName());
		input.setStrNameQualifier("My Website");
		input.setSessionId("abcdedf1234567");
		
		HashMap<String, String> customAttributes = new HashMap<String,String>();
		customAttributes.put("RequesterName", user.getName());
		customAttributes.put("RequestReason", "Patient is bleeding.");
		customAttributes.put("PatientFirstName", "Hodor");
		customAttributes.put("PatientLastName", "Guy");
		customAttributes.put("PatientSSN", "123456789");
		input.setAttributes(customAttributes);
		
		String samlMessage = null;
		
		try {
			samlMessage = samlGenerator.createSAML(input);
		} catch (MarshallingException e) {
			e.printStackTrace();
		}
		
		String result = "";
		if(cacheOnly == null || cacheOnly.booleanValue() == false) {
			result = docManager.getDocumentById(samlMessage, documentId);
		} else {
			docManager.getDocumentById(samlMessage, documentId);
		}
		return result;
	}
}
