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
public class PatientSearchService extends EHealthQueryService implements Callable {
	
	private static final Logger logger = LogManager.getLogger(PatientSearchService.class);
	private Query query;
	
	@Value("${delaySeconds}")
	private String delaySeconds;
	@Value("${timeout}")
	private String timeout;

	public void searchForPatientWithTerms(RestTemplate restTemplate, PatientSearch patientSearch){
		this.setRestTemplate(restTemplate);
		this.setAuthorizationHeader();
		HttpEntity<PatientSearch> patientSearchRequest = new HttpEntity<PatientSearch>(patientSearch, this.getHeaders());
		Query queryResponse = restTemplate.postForObject("http://localhost:" + this.getPort() + "/search", patientSearchRequest, Query.class);
		logger.info("Request sent to broker from services REST with patient search " + patientSearchRequest.toString());

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
	
	public List<QueryOrganization> waitForOrgs(List<QueryOrganization> queryOrgs) throws InterruptedException{
		boolean waitingForOrgs = true;
		int runs = 0;
		while(waitingForOrgs && runs < Integer.parseInt(timeout)){
			if(areOrgsComplete(queryOrgs)){
				return queryOrgs;
			}else{
				runs++;
				Thread.sleep(Integer.parseInt(delaySeconds) * 1000);
				HttpEntity<Patient[]> entity = new HttpEntity<Patient[]>(this.getHeaders());
				HttpEntity<Query> response = this.getRestTemplate().exchange("http://localhost:" + this.getPort() + "/queries/" + this.query.getId() , HttpMethod.GET, entity, Query.class);
				logger.info("Query "  + this.query.getId() + " request sent to broker from services REST.");
				queryOrgs = response.getBody().getOrgStatuses();
			}
		}
		return queryOrgs;
	}
	
	@Override
	public List<QueryOrganization> call() throws Exception {
		return waitForOrgs(this.query.getOrgStatuses());
	}
	
}
