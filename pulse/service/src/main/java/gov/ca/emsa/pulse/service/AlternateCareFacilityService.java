package gov.ca.emsa.pulse.service;

import gov.ca.emsa.pulse.common.domain.AlternateCareFacility;
import gov.ca.emsa.pulse.auth.user.JWTAuthenticatedUser;

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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class AlternateCareFacilityService {

	private static final Logger logger = LogManager.getLogger(AlternateCareFacilityService.class);

	@Value("${brokerUrl}")
	private String brokerUrl;

	// POST - create an alternate care facility
	@RequestMapping(value = "/acfs/create", method = RequestMethod.POST)
	public AlternateCareFacility createACF(@RequestBody AlternateCareFacility acf) throws JsonProcessingException {

		RestTemplate query = new RestTemplate();
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
		ObjectMapper mapper = new ObjectMapper();


		JWTAuthenticatedUser jwtUser = (JWTAuthenticatedUser) SecurityContextHolder.getContext().getAuthentication();
		AlternateCareFacility returnAcf = null;
		if(jwtUser == null){
			logger.error("Could not find a logged in user. ");
		}else{
			headers.add("User", mapper.writeValueAsString(jwtUser));
			HttpEntity<AlternateCareFacility> request = new HttpEntity<AlternateCareFacility>(acf, headers);
			returnAcf = query.postForObject(brokerUrl + "/acfs/create", request, AlternateCareFacility.class);
			logger.info("Request sent to broker from services REST.");
		}
		return returnAcf;
	}

	// GET all acfs
	@RequestMapping(value = "/acfs")
	public ArrayList<AlternateCareFacility> getACFs() throws JsonProcessingException {

		RestTemplate query = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		ObjectMapper mapper = new ObjectMapper();

		JWTAuthenticatedUser jwtUser = (JWTAuthenticatedUser) SecurityContextHolder.getContext().getAuthentication();
		ArrayList<AlternateCareFacility> acfList = null;
		if(jwtUser == null){
			logger.error("Could not find a logged in user. ");
		}else{
			headers.set("User", mapper.writeValueAsString(jwtUser));
			HttpEntity<AlternateCareFacility[]> entity = new HttpEntity<AlternateCareFacility[]>(headers);
			HttpEntity<AlternateCareFacility[]> response = query.exchange(brokerUrl + "/acfs", HttpMethod.GET, entity, AlternateCareFacility[].class);
			logger.info("Request sent to broker from services REST.");
			acfList = new ArrayList<AlternateCareFacility>(Arrays.asList(response.getBody()));
		}
		return acfList;

	}

	// get acf by its id
	@RequestMapping(value = "/acfs/{id}")
	public AlternateCareFacility getACFById(@PathVariable Long id) throws JsonProcessingException {

		RestTemplate query = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		ObjectMapper mapper = new ObjectMapper();

		JWTAuthenticatedUser jwtUser = (JWTAuthenticatedUser) SecurityContextHolder.getContext().getAuthentication();
		HttpEntity<AlternateCareFacility> response = null;
		if(jwtUser == null){
			logger.error("Could not find a logged in user. ");
		}else{
			headers.set("User", mapper.writeValueAsString(jwtUser));
			HttpEntity<AlternateCareFacility> entity = new HttpEntity<AlternateCareFacility>(headers);
			response = query.exchange(brokerUrl + "/acfs" + id, HttpMethod.GET, entity, AlternateCareFacility.class);
			logger.info("Request sent to broker from services REST.");
		}
		return response.getBody();
	}

		// edit an acf by its id
	@RequestMapping(value = "/acfs/{acfId}/edit")
	public AlternateCareFacility editACFById(@PathVariable Long acfId) throws JsonProcessingException {

		RestTemplate query = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		ObjectMapper mapper = new ObjectMapper();

		JWTAuthenticatedUser jwtUser = (JWTAuthenticatedUser) SecurityContextHolder.getContext().getAuthentication();
		AlternateCareFacility returnAcf = null;
		if(jwtUser == null){
			logger.error("Could not find a logged in user. ");
		}else{
			headers.set("User", mapper.writeValueAsString(jwtUser));
			HttpEntity<AlternateCareFacility> request = new HttpEntity<AlternateCareFacility>(headers);
			returnAcf = query.postForObject(brokerUrl + "/acfs/" + acfId + "/edit", request, AlternateCareFacility.class);
			logger.info("Request sent to broker from services REST.");
		}

		return returnAcf;
	}
}
