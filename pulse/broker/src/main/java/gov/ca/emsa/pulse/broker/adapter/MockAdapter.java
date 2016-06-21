package gov.ca.emsa.pulse.broker.adapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import gov.ca.emsa.pulse.broker.domain.Patient;
import gov.ca.emsa.pulse.broker.dto.OrganizationDTO;
import gov.ca.emsa.pulse.broker.dto.PatientDTO;
import gov.ca.emsa.pulse.service.PatientService;

@Component
public class MockAdapter implements Adapter {
	private static final Logger logger = LogManager.getLogger(MockAdapter.class);
	private DateFormat formatter;

	public MockAdapter() {
		formatter = new SimpleDateFormat(PatientService.dobFormat);
	}
	
	@Override
	public Patient[] queryPatients(OrganizationDTO org, PatientDTO toSearch, String samlMessage) {
		String postUrl = org.getEndpointUrl() + "/patients";
		MultiValueMap<String,String> parameters = new LinkedMultiValueMap<String,String>();
		parameters.add("firstName", toSearch.getFirstName());
		parameters.add("lastName", toSearch.getLastName());
		if(toSearch.getDateOfBirth() != null) {
			parameters.add("dob", formatter.format(toSearch.getDateOfBirth()));
		}
		parameters.add("gender", toSearch.getGender());
		parameters.add("ssn", toSearch.getSsn());
		if(toSearch.getAddress() != null) {
			parameters.add("zipcode", toSearch.getAddress().getZipcode());
		}
		parameters.add("samlMessage", samlMessage);
		RestTemplate restTemplate = new RestTemplate();
		Patient[] searchResults = null;
		try {
			searchResults = restTemplate.postForObject(postUrl, parameters, Patient[].class);
		} catch(Exception ex) {
			logger.error("Exception when querying " + postUrl, ex);
			throw ex;
		}
		return searchResults;
	}

}
