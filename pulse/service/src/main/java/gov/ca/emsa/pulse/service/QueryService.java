package gov.ca.emsa.pulse.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

@Service
public class QueryService {
	private static final Logger logger = LogManager.getLogger(QueryService.class);
	
	// get all queries that belong to the logged in user
	@RequestMapping(value = "/queries")
	public List<Query> getQueries() {
		
		String patientUrl = "http://localhost:8090/queries";
		RestTemplate query = new RestTemplate();
		
		Authentication auth =  SecurityContextHolder.getContext().getAuthentication();
		User user = new User();
		if(auth != null){
			user.setUserToken(auth.getPrincipal().toString());
			user.setName(auth.getName());
		}else{
			logger.error("Could not find a logged in user. ");
		}
		
		Query[] queryList = query.postForObject(patientUrl, user, Query[].class);
		ArrayList<Query> queries = new ArrayList<Query>(Arrays.asList(queryList));
        
        return queries;
	}
	
	// get the query that has the id = queryId
	@RequestMapping(value = "/queries/{queryId}")
	public Query getQueryWithId(@PathVariable Long queryId) {
		
		String patientUrl = "http://localhost:8090/queries/" + queryId;
		RestTemplate query = new RestTemplate();
		
		Authentication auth =  SecurityContextHolder.getContext().getAuthentication();
		User user = new User();
		if(auth != null){
			user.setName(auth.getName());
		}else{
			logger.error("Could not find a logged in user. ");
		}
		
		Query queryRet = query.postForObject(patientUrl, user, Query.class);
        
        return queryRet;
	}

	// stages a patient in the database from a query id
	@RequestMapping(value = "/queries/{queryId}/stage")
	public Query stageQueryWithId(@RequestBody ArrayList<Long> patientIds, @PathVariable Long queryId) {

		String patientUrl = "http://localhost:8090/queries/" + queryId + "/stage";
		RestTemplate query = new RestTemplate();
		UserAndPatientIds uap = new UserAndPatientIds();
		uap.setPatientIds(patientIds);

		Authentication auth =  SecurityContextHolder.getContext().getAuthentication();
		User user = new User();
		if(auth != null){
			user.setName(auth.getName());
			uap.setUser(user);
		}else{
			logger.error("Could not find a logged in user. ");
		}

		Query queryRet = query.postForObject(patientUrl, uap, Query.class);

		return queryRet;
	}
}
