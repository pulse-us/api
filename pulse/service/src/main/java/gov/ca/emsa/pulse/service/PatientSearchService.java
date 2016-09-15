package gov.ca.emsa.pulse.service;

import gov.ca.emsa.pulse.common.domain.PatientSearch;
import gov.ca.emsa.pulse.common.domain.QueryStatus;
import gov.ca.emsa.pulse.common.domain.Patient;
import gov.ca.emsa.pulse.common.domain.Query;
import gov.ca.emsa.pulse.common.domain.QueryOrganization;
import gov.ca.emsa.pulse.service.controller.PatientDiscoveryController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class PatientSearchService implements Callable{
	private static final Logger logger = LogManager.getLogger(PatientSearchService.class);

	@Value("${server.port}")
	private String port;

	@Value("${samlServiceUrl}")
	private String samlServiceUrl;

	private Query query;
	private MultiValueMap<String, String> headers;

	public void searchForPatientWithTerms(PatientSearch patientSearch){
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
		ObjectMapper mapper = new ObjectMapper();
		HttpEntity<String> jwtResponse = null;

		// get fake jwt to make broker requests
		HttpHeaders jwtHeader = new HttpHeaders();
		jwtHeader.setContentType(MediaType.TEXT_XML);
		HttpEntity<String> jwtRequest = new HttpEntity<String>(jwtHeader);
		RestTemplate jwtQuery = new RestTemplate();
		jwtResponse = jwtQuery.exchange(samlServiceUrl + "/jwt", HttpMethod.GET, jwtRequest , String.class);
		String jwt = jwtResponse.getBody();

		try {
			headers.add("Authorization", mapper.writeValueAsString("Bearer " + jwt));
		} catch (JsonProcessingException e) {
			logger.error(e);
		}
		headers.add("Content-Type", "application/xml");
		this.headers = headers;
		HttpEntity<PatientSearch> patientSearchRequest = new HttpEntity<PatientSearch>(patientSearch, headers);
		RestTemplate query = new RestTemplate();
		Query queryResponse = query.postForObject("http://localhost:" + port + "/search", patientSearchRequest, Query.class);
		logger.info("Request sent to broker from services REST.");

		this.query = queryResponse;
	}
	
	public boolean areOrgsComplete(List<QueryOrganization> queryOrgs){
		for(QueryOrganization queryOrg: queryOrgs){
			if(queryOrg.getStatus().equals(QueryStatus.ACTIVE.name())){
				return false;
			}
		}
		return true;
	}
	
	public List<QueryOrganization> waitForOrgs(List<QueryOrganization> queryOrgs){
		boolean waitingForOrgs = true;
		while(waitingForOrgs){
			if(areOrgsComplete(queryOrgs)){
				return queryOrgs;
			}else{
				HttpEntity<PatientSearch> patientSearchRequest = new HttpEntity<PatientSearch>(headers);
				RestTemplate query = new RestTemplate();
				Query queryResponse = query.postForObject("http://localhost:" + port + "/queries" + this.query.getId() , patientSearchRequest, Query.class);
				logger.info("Request sent to broker from services REST.");
				queryOrgs = queryResponse.getOrgStatuses();
			}
		}
		return null;
	}
	
	@Override
	public List<QueryOrganization> call() throws Exception {
		return waitForOrgs(this.query.getOrgStatuses());
	}
	
}
