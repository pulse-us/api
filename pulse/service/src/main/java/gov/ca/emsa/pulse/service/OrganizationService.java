package gov.ca.emsa.pulse.service;

import gov.ca.emsa.pulse.auth.user.JWTAuthenticatedUser;
import gov.ca.emsa.pulse.common.domain.Organization;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.DeserializationFeature;

@Api(value="Organizations")
@RestController
public class OrganizationService {

	private static final Logger logger = LogManager.getLogger(OrganizationService.class);

	@Value("${brokerUrl}")
	private String brokerUrl;

	// get all organizations
	@ApiOperation(value="Get all organizations.")
	@RequestMapping(value = "/organizations", method = RequestMethod.GET)
	public ArrayList<Organization> getOrganizations() throws JsonProcessingException {

		RestTemplate query = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		ObjectMapper mapper = new ObjectMapper();

		JWTAuthenticatedUser jwtUser = (JWTAuthenticatedUser) SecurityContextHolder.getContext().getAuthentication();
		ArrayList<Organization> orgList = null;
		if(jwtUser == null){
			logger.error("Could not find a logged in user. ");
		}else{
            //mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
			headers.set("User", mapper.writeValueAsString(jwtUser));
			HttpEntity<Organization[]> entity = new HttpEntity<Organization[]>(headers);
			HttpEntity<Organization[]> response = query.exchange(brokerUrl + "/organizations", HttpMethod.GET, entity, Organization[].class);
			logger.info("Request sent to broker from services REST.");
			orgList = new ArrayList<Organization>(Arrays.asList(response.getBody()));
		}
		return orgList;
	}
}
