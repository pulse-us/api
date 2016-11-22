package gov.ca.emsa.pulse.service;

import gov.ca.emsa.pulse.common.domain.Patient;
import gov.ca.emsa.pulse.common.domain.PatientSearch;
import gov.ca.emsa.pulse.common.domain.Query;
import gov.ca.emsa.pulse.common.domain.QueryLocationMap;
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
	
	public boolean areOrgsComplete(List<QueryLocationMap> queryOrgs){
		for(QueryLocationMap queryOrg: queryOrgs){
			if(queryOrg.getStatus().equals(QueryStatus.ACTIVE.name())){
				return false;
			}
		}
		return true;
	}
	
	public List<QueryLocationMap> waitForOrgs(List<QueryLocationMap> queryOrgs) throws InterruptedException{
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
				queryOrgs = response.getBody().getLocationStatuses();
			}
		}
		return queryOrgs;
	}
	
	@Override
	public List<QueryLocationMap> call() throws Exception {
		return waitForOrgs(this.query.getLocationStatuses());
	}
	
}
