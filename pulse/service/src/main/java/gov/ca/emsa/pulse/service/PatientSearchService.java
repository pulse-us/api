package gov.ca.emsa.pulse.service;

import gov.ca.emsa.pulse.common.domain.Patient;
import gov.ca.emsa.pulse.common.domain.PatientSearch;
import gov.ca.emsa.pulse.common.domain.Query;
import gov.ca.emsa.pulse.common.domain.QueryEndpointMap;
import gov.ca.emsa.pulse.common.domain.QueryStatus;

import java.util.List;
import java.util.concurrent.Callable;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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
	
	public boolean areLocationsComplete(List<QueryEndpointMap> queryLocs){
		for(QueryEndpointMap queryLoc : queryLocs){
			if(queryLoc.getStatus().equals(QueryStatus.Active.name())){
				return false;
			}
		}
		return true;
	}
	
	public List<QueryEndpointMap> waitForLocations(List<QueryEndpointMap> queryLocs) throws InterruptedException{
		boolean waitingForLocs = true;
		int runs = 0;
		while(waitingForLocs && runs < Integer.parseInt(timeout)){
			if(areLocationsComplete(queryLocs)){
				return queryLocs;
			}else{
				runs++;
				Thread.sleep(Integer.parseInt(delaySeconds) * 1000);
				HttpEntity<Patient[]> entity = new HttpEntity<Patient[]>(this.getHeaders());
				HttpEntity<Query> response = this.getRestTemplate().exchange("http://localhost:" + this.getPort() + "/queries/" + this.query.getId() , HttpMethod.GET, entity, Query.class);
				logger.info("Query "  + this.query.getId() + " request sent to broker from services REST.");
				queryLocs = response.getBody().getEndpointStatuses();
			}
		}
		return queryLocs;
	}
	
	@Override
	public List<QueryEndpointMap> call() throws Exception {
		return waitForLocations(this.query.getEndpointStatuses());
	}
	
}
