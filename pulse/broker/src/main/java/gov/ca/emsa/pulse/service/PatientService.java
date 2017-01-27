package gov.ca.emsa.pulse.service;

import gov.ca.emsa.pulse.auth.user.CommonUser;
import gov.ca.emsa.pulse.broker.audit.AuditEvent;
import gov.ca.emsa.pulse.broker.domain.QueryType;
import gov.ca.emsa.pulse.broker.dto.AlternateCareFacilityDTO;
import gov.ca.emsa.pulse.broker.dto.AuditEventDTO;
import gov.ca.emsa.pulse.broker.dto.AuditHumanRequestorDTO;
import gov.ca.emsa.pulse.broker.dto.AuditQueryParametersDTO;
import gov.ca.emsa.pulse.broker.dto.AuditRequestDestinationDTO;
import gov.ca.emsa.pulse.broker.dto.AuditRequestSourceDTO;
import gov.ca.emsa.pulse.broker.dto.AuditSourceDTO;
import gov.ca.emsa.pulse.broker.dto.DocumentDTO;
import gov.ca.emsa.pulse.broker.dto.DomainToDtoConverter;
import gov.ca.emsa.pulse.broker.dto.DtoToDomainConverter;
import gov.ca.emsa.pulse.broker.dto.PatientDTO;
import gov.ca.emsa.pulse.broker.manager.AlternateCareFacilityManager;
import gov.ca.emsa.pulse.broker.manager.AuditEventManager;
import gov.ca.emsa.pulse.broker.manager.DocumentManager;
import gov.ca.emsa.pulse.broker.manager.PatientManager;
import gov.ca.emsa.pulse.broker.saml.SAMLInput;
import gov.ca.emsa.pulse.broker.saml.SamlGenerator;
import gov.ca.emsa.pulse.common.domain.AlternateCareFacility;
import gov.ca.emsa.pulse.common.domain.Document;
import gov.ca.emsa.pulse.common.domain.Patient;
import gov.ca.emsa.pulse.common.soap.JSONToSOAPService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.InvalidParameterException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Api(value = "patients")
@RestController
@RequestMapping("/patients")
public class PatientService {
	private static final Logger logger = LogManager.getLogger(PatientService.class);
	@Autowired SamlGenerator samlGenerator;
	@Autowired private PatientManager patientManager;
	@Autowired private DocumentManager docManager;
	@Autowired private AlternateCareFacilityManager acfManager;
	@Autowired private AuditEventManager auditManager;
	public PatientService() {
	}

	@ApiOperation(value="Get all patients at the logged-in user's ACF")
	@RequestMapping("")
	public List<Patient> getPatientsAtAcf() throws InvalidParameterException, UnknownHostException {
		CommonUser user = UserUtil.getCurrentUser();
		if(user.getAcf() == null || user.getAcf().getId() == null) {
			throw new InvalidParameterException("There was no ACF supplied in the User header, or the ACF had a null ID.");
		}
		List<PatientDTO> queryResults = patientManager.getPatientsAtAcf(user.getAcf().getId());
		List<Patient> results = new ArrayList<Patient>(queryResults.size());
        for(PatientDTO patientDto : queryResults) {
        	Patient patient = DtoToDomainConverter.convert(patientDto);
        	results.add(patient);
        }

        return results;
	}

	@ApiOperation(value="Get a list of documents associated with the given patient")
	@RequestMapping("/{patientId}/documents")
	public List<Document> getDocumentListForPatient(@PathVariable("patientId")Long patientId) {
		CommonUser user = UserUtil.getCurrentUser();
		//auditManager.addAuditEntry(QueryType.SEARCH_DOCUMENT, "/" + patientId + "/documents", user.getSubjectName());
		List<DocumentDTO> docDtos = docManager.getDocumentsForPatient(patientId);
		List<Document> results = new ArrayList<Document>(docDtos.size());
		for(DocumentDTO docDto : docDtos) {
			results.add(DtoToDomainConverter.convert(docDto));
		}
		return results;
	}

	@ApiOperation(value="Retrieve a specific document from a location.")
	@RequestMapping(value = "/{patientId}/documents/{documentId}")
	public @ResponseBody String getDocumentContents(@PathVariable("patientId") Long patientId,
			@PathVariable("documentId") Long documentId,
			@RequestParam(value="cacheOnly", required= false, defaultValue="true") Boolean cacheOnly) 
		throws SQLException, JsonProcessingException {
		
		CommonUser user = UserUtil.getCurrentUser();
		//auditManager.addAuditEntry(QueryType.CACHE_DOCUMENT, "/" + patientId + "/documents/" + documentId, user.getSubjectName());
		SAMLInput input = new SAMLInput();
		input.setStrIssuer(user.getFirstName());
		input.setStrNameID(user.getFirstName());
		input.setStrNameQualifier("My Website");
		input.setSessionId("abcdedf1234567");

		HashMap<String, String> customAttributes = new HashMap<String,String>();
		customAttributes.put("RequesterName", user.getFirstName());
		customAttributes.put("RequestReason", "Patient is bleeding.");
		customAttributes.put("PatientGivenName", "Hodor");
		customAttributes.put("PatientFamilyName", "Guy");
		customAttributes.put("PatientSSN", "123456789");
		input.setAttributes(customAttributes);

		String result = "";
		if(cacheOnly == null || cacheOnly.booleanValue() == false) {
			result = docManager.getDocumentById(user, input, documentId);
		} else {
			docManager.getDocumentById(user, input, documentId);
		}
		return result;
	}
	
	@ApiOperation(value = "Edit a patient's information")
	@RequestMapping(value = "/{patientId}/edit", method = RequestMethod.POST)
	public Patient update(@PathVariable("patientId") Long patientId, 
			@RequestBody(required=true) Patient toUpdate) throws SQLException {
		
		PatientDTO patientToUpdate = DomainToDtoConverter.convertToPatient(toUpdate);
		patientToUpdate.setId(patientId);
		PatientDTO updated = patientManager.update(patientToUpdate);
		return DtoToDomainConverter.convert(updated);
	}
	
	@ApiOperation(value = "Delete a patient")
	@RequestMapping(value="/{patientId}/delete", method = RequestMethod.POST)
	public void deletePatient(@PathVariable(value="patientId") Long patientId) 
	 throws SQLException, JsonProcessingException {
		patientManager.delete(patientId);
	}
}
