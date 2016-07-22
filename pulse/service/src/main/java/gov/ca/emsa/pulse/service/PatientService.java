package gov.ca.emsa.pulse.service;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import gov.ca.emsa.pulse.auth.user.CommonUser;
import gov.ca.emsa.pulse.auth.user.JWTAuthenticatedUser;
import gov.ca.emsa.pulse.common.domain.Patient;
import gov.ca.emsa.pulse.common.domain.PatientSearch;
import gov.ca.emsa.pulse.common.domain.Query;
import io.swagger.annotations.ApiOperation;

@RestController
public class PatientService {
	private static final Logger logger = LogManager.getLogger(PatientService.class);
	@Autowired private Environment env;
	
	@Value("${brokerUrl}")
	private String brokerUrl;

	@ApiOperation(value="Search for patients that match the parameters.")
	@RequestMapping(value = "/search", method = RequestMethod.POST)
	public void searchPatients(@RequestBody PatientSearch patientSearchTerms) throws JsonProcessingException {

		MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
		ObjectMapper mapper = new ObjectMapper();

		JWTAuthenticatedUser jwtUser = (JWTAuthenticatedUser) SecurityContextHolder.getContext().getAuthentication();
		if(jwtUser == null){
			logger.error("Could not find a logged in user. ");
		}else{
			headers.add("User", mapper.writeValueAsString(jwtUser));
			HttpEntity<PatientSearch> request = new HttpEntity<PatientSearch>(patientSearchTerms, headers);
			RestTemplate query = new RestTemplate();
			query.postForObject(brokerUrl + "/search", request, Query.class);
			logger.info("Request sent to broker from services REST.");
		}

	}

	// get all patients from the logged in users ACF
	@RequestMapping(value = "/patients")
	public ArrayList<Patient> getAllPatientsAtACF() throws JsonProcessingException {

		RestTemplate query = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		ObjectMapper mapper = new ObjectMapper();

		JWTAuthenticatedUser jwtUser = (JWTAuthenticatedUser) SecurityContextHolder.getContext().getAuthentication();
		ArrayList<Patient> patientList = null;
		if(jwtUser == null){
			logger.error("Could not find a logged in user. ");
		}else{
			headers.set("User", mapper.writeValueAsString(jwtUser));
			HttpEntity<Patient[]> entity = new HttpEntity<Patient[]>(headers);
			HttpEntity<Patient[]> response = query.exchange(brokerUrl + "/patients", HttpMethod.GET, entity, Patient[].class);
			logger.info("Request sent to broker from services REST.");
			patientList = new ArrayList<Patient>(Arrays.asList(response.getBody()));
		}
		
		return patientList;
	}
}