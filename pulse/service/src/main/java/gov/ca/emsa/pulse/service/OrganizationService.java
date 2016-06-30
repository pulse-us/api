package gov.ca.emsa.pulse.service;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
@RestController
public class OrganizationService {
	
	private static final Logger logger = LogManager.getLogger(OrganizationService.class);
	
	@Value("${getOrganizationsUrl}")
	private String getOrganizationsUrl;
	
	// get all organizations
	@RequestMapping(value = "/organizations")
	public ArrayList<Organization> getACFs() throws JsonProcessingException {

		RestTemplate query = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		ObjectMapper mapper = new ObjectMapper();

		Authentication auth =  SecurityContextHolder.getContext().getAuthentication();
		User user = new User();
		if(auth != null){
			user.setName(auth.getName());
		}else{
			logger.error("Could not find a logged in user. ");
		}

		headers.set("User", mapper.writeValueAsString(user));
		HttpEntity<Organization[]> entity = new HttpEntity<Organization[]>(headers);
		HttpEntity<Organization[]> response = query.exchange(getOrganizationsUrl, HttpMethod.GET, entity, Organization[].class);
		logger.info("Request sent to broker from services REST.");
		ArrayList<Organization> orgList = new ArrayList<Organization>(Arrays.asList(response.getBody()));

		return orgList;
	}
}
