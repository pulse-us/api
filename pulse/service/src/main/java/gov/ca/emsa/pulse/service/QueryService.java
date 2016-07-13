package gov.ca.emsa.pulse.service;

import gov.ca.emsa.pulse.auth.user.JWTAuthenticatedUser;
import gov.ca.emsa.pulse.common.domain.Query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class QueryService {
	private static final Logger logger = LogManager.getLogger(QueryService.class);
	
	@Value("${brokerUrl}")
	private String brokerUrl;
	
	// get all queries that belong to the logged in user
	@RequestMapping(value = "/queries")
	public List<Query> getQueries() throws JsonProcessingException {

		RestTemplate query = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		ObjectMapper mapper = new ObjectMapper();

		Authentication auth =  SecurityContextHolder.getContext().getAuthentication();
		JWTAuthenticatedUser user = new JWTAuthenticatedUser();
		if(auth != null){
			user.setJwt(auth.getPrincipal().toString());
			user.setSubjectName(auth.getName());
		}else{
			logger.error("Could not find a logged in user. ");
		}

		headers.set("User", mapper.writeValueAsString(user));
		HttpEntity<Query[]> entity = new HttpEntity<Query[]>(headers);
		HttpEntity<Query[]> response = query.exchange(brokerUrl + "/queries", HttpMethod.GET, entity, Query[].class);
		logger.info("Request sent to broker from services REST.");
		ArrayList<Query> queryList = new ArrayList<Query>(Arrays.asList(response.getBody()));

		return queryList;
	}

	// get the query that has the id = queryId
	@RequestMapping(value = "/queries/{queryId}")
	public Query getQueryWithId(@PathVariable Long queryId) throws JsonProcessingException {

		RestTemplate query = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		ObjectMapper mapper = new ObjectMapper();

		Authentication auth =  SecurityContextHolder.getContext().getAuthentication();
		JWTAuthenticatedUser user = new JWTAuthenticatedUser();
		if(auth != null){
			user.setSubjectName(auth.getName());
		}else{
			logger.error("Could not find a logged in user. ");
		}

		headers.set("User", mapper.writeValueAsString(user));
		HttpEntity<Query> entity = new HttpEntity<Query>(headers);
		HttpEntity<Query> response = query.exchange(brokerUrl + "/queries/" + queryId, HttpMethod.GET, entity, Query.class);
		logger.info("Request sent to broker from services REST.");
		return response.getBody();
	}

	// stages a patient in the database from a query id
	@RequestMapping(value = "/queries/{queryId}/stage", method = RequestMethod.POST)
	public Query stageQueryWithId(@RequestBody ArrayList<Long> patientIds, @PathVariable Long queryId) throws JsonProcessingException {

		RestTemplate query = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		ObjectMapper mapper = new ObjectMapper();

		Authentication auth =  SecurityContextHolder.getContext().getAuthentication();
		JWTAuthenticatedUser user = new JWTAuthenticatedUser();
		if(auth != null){
			user.setSubjectName(auth.getName());
		}else{
			logger.error("Could not find a logged in user. ");
		}

		headers.add("User", mapper.writeValueAsString(user));
		HttpEntity<ArrayList<Long>> request = new HttpEntity<ArrayList<Long>>(patientIds, headers);
		Query queryRet = query.postForObject(brokerUrl + "/queries/" + queryId + "/stage", request, Query.class);
		logger.info("Request sent to broker from services REST.");
		return queryRet;
	}
}
