package gov.ca.emsa.pulse.broker.adapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.hl7.v3.PRPAIN201305UV02;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import gov.ca.emsa.pulse.broker.dto.OrganizationDTO;
import gov.ca.emsa.pulse.broker.dto.PatientOrganizationMapDTO;
import gov.ca.emsa.pulse.broker.dto.PatientRecordDTO;
import gov.ca.emsa.pulse.broker.dto.SearchResultConverter;
import gov.ca.emsa.pulse.common.domain.Document;
import gov.ca.emsa.pulse.common.domain.Patient;
import gov.ca.emsa.pulse.common.domain.PatientSearch;

@Component
public class EHealthAdapter implements Adapter {
	private static final Logger logger = LogManager.getLogger(EHealthAdapter.class);
	
	@Override
	public List<PatientRecordDTO> queryPatients(OrganizationDTO org, PatientSearch toSearch, String samlMessage) {
		PRPAIN201305UV02 patientDiscoveryRequest = new PRPAIN201305UV02();

		String postUrl = org.getEndpointUrl() + "/patientDiscovery";
		MultiValueMap<String,String> parameters = new LinkedMultiValueMap<String,String>();
		parameters.add("givenName", toSearch.getGivenName());
		parameters.add("familyName", toSearch.getFamilyName());
		parameters.add("dob", toSearch.getDob());
		parameters.add("gender", toSearch.getGender());
		parameters.add("ssn", toSearch.getSsn());
		parameters.add("zipcode", toSearch.getZip());
		parameters.add("samlMessage", samlMessage);
		RestTemplate restTemplate = new RestTemplate();
		Patient[] searchResults = null;
		try {
			searchResults = restTemplate.postForObject(postUrl, parameters, Patient[].class);
		} catch(Exception ex) {
			logger.error("Exception when querying " + postUrl, ex);
			throw ex;
		}
		List<PatientRecordDTO> records = new ArrayList<PatientRecordDTO>();
		for(int i = 0; i < searchResults.length; i++) {
			PatientRecordDTO record = SearchResultConverter.convertToPatientRecord(searchResults[i]);
			records.add(record);
		}
		return records;
	}

	@Override
	public Document[] queryDocuments(OrganizationDTO org, PatientOrganizationMapDTO toSearch, String samlMessage) {
		String postUrl = org.getEndpointUrl() + "/documents";
		MultiValueMap<String,String> parameters = new LinkedMultiValueMap<String,String>();
		parameters.add("patientId", toSearch.getOrgPatientId());
		parameters.add("samlMessage", samlMessage);
		RestTemplate restTemplate = new RestTemplate();
		Document[] searchResults = null;
		try {
			searchResults = restTemplate.postForObject(postUrl, parameters, Document[].class);
		} catch(Exception ex) {
			logger.error("Exception when querying " + postUrl, ex);
			throw ex;
		}
		return searchResults;
	}
}
