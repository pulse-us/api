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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import gov.ca.emsa.pulse.common.domain.CommonUser;
import gov.ca.emsa.pulse.common.domain.CreatePatientRequest;
import gov.ca.emsa.pulse.common.domain.Patient;
import gov.ca.emsa.pulse.common.domain.Query;
import gov.ca.emsa.pulse.broker.dto.AlternateCareFacilityDTO;
import gov.ca.emsa.pulse.broker.dto.DomainToDtoConverter;
import gov.ca.emsa.pulse.broker.dto.DtoToDomainConverter;
import gov.ca.emsa.pulse.broker.dto.PatientDTO;
import gov.ca.emsa.pulse.broker.dto.PatientOrganizationMapDTO;
import gov.ca.emsa.pulse.broker.dto.QueryDTO;
import gov.ca.emsa.pulse.broker.manager.AlternateCareFacilityManager;
import gov.ca.emsa.pulse.broker.manager.DocumentManager;
import gov.ca.emsa.pulse.broker.manager.PatientManager;
import gov.ca.emsa.pulse.broker.manager.QueryManager;
import gov.ca.emsa.pulse.broker.saml.SAMLInput;
import gov.ca.emsa.pulse.broker.saml.SamlGenerator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "queryStatus")
@RestController
@RequestMapping("/queries")
public class QueryService {
	private static final Logger logger = LogManager.getLogger(QueryService.class);
	@Autowired private SamlGenerator samlGenerator;
	@Autowired QueryManager queryManager;
	@Autowired PatientManager patientManager;
	@Autowired DocumentManager docManager;
	@Autowired AlternateCareFacilityManager acfManager;

	@ApiOperation(value = "Get all queries for the logged-in user")
	@RequestMapping(value="", method = RequestMethod.GET)
	public List<Query> getQueries() {
		CommonUser user = UserUtil.getCurrentUser();

		List<QueryDTO> queries = queryManager.getAllQueriesForUser(user.getSubjectName());
		List<Query> results = new ArrayList<Query>();
		for(QueryDTO query : queries) {
			results.add(DtoToDomainConverter.convert(query));
		}
		return results;
	}

	@ApiOperation(value="Get the status of a query")
	@RequestMapping(value="/{queryId}", method = RequestMethod.GET)
    public Query getQueryStatus(@PathVariable(value="queryId") Long queryId) {
       QueryDTO initiatedQuery = queryManager.getById(queryId);
       return DtoToDomainConverter.convert(initiatedQuery);
    }

	@ApiOperation(value="Create a Patient from multiple PatientRecords")
	@RequestMapping(value="/{queryId}/stage", method = RequestMethod.POST)
    public Patient stagePatientFromResults(@PathVariable(value="queryId") Long queryId,
    		@RequestBody CreatePatientRequest request) throws InvalidParameterException {
		CommonUser user = UserUtil.getCurrentUser();
		if(request.getPatient() == null ||
				request.getPatientRecordIds() == null ||
				request.getPatientRecordIds().size() == 0) {
			throw new InvalidParameterException("A patient object and at least one patient record id is required.");
		}

		//create a new Patient
		PatientDTO patientToCreate = DomainToDtoConverter.convertToPatient(request.getPatient());
		AlternateCareFacilityDTO acfDto = acfManager.getByName(user.getAcf());
		if(acfDto == null || acfDto.getId() == null) {
			throw new InvalidParameterException("The ACF supplied, '" + user.getAcf() + "' was not found in the database.");
		}
		patientToCreate.setAcf(acfDto);

		PatientDTO patient = patientManager.create(patientToCreate);

		//create patient organization mappings based on the patientrecords we are using
		for(Long patientRecordId : request.getPatientRecordIds()) {
			PatientOrganizationMapDTO orgMapDto = patientManager.createOrganizationMapFromPatientRecord(patient, patientRecordId);

			//kick off document list retrieval service
			SAMLInput input = new SAMLInput();
			input.setStrIssuer("https://idp.dhv.gov");
			input.setStrNameID("UserBrianLindsey");
			input.setStrNameQualifier("My Website");
			input.setSessionId("abcdedf1234567");
			HashMap<String, String> customAttributes = new HashMap<String,String>();
			customAttributes.put("RequesterFirstName", user.getFirstName());
			customAttributes.put("RequestReason", "Get patient documents");
			customAttributes.put("PatientId", orgMapDto.getOrgPatientId());
			input.setAttributes(customAttributes);

			String samlMessage = null;
			try {
				samlMessage = samlGenerator.createSAML(input);
			} catch (MarshallingException e) {
				logger.error("Could not create SAML from input " + input, e);
			}
			patient.getOrgMaps().add(orgMapDto);
			docManager.queryForDocuments(samlMessage, orgMapDto);
		}

		//delete query (all associated items should cascade)
		queryManager.delete(queryId);
		return DtoToDomainConverter.convert(patient);
    }
}
