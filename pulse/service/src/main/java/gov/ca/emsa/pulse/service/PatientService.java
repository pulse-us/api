package gov.ca.emsa.pulse.service;
import java.util.ArrayList;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.mortbay.jetty.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import gov.ca.emsa.pulse.auth.user.CommonUser;
import gov.ca.emsa.pulse.auth.user.JWTAuthenticatedUser;
import gov.ca.emsa.pulse.common.domain.Patient;
import gov.ca.emsa.pulse.common.domain.PatientSearch;
import gov.ca.emsa.pulse.common.domain.Query;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value="Patients")
@RestController
public class PatientService {
	private static final Logger logger = LogManager.getLogger(PatientService.class);
	@Autowired private Environment env;
	
	@Value("${brokerUrl}")
	private String brokerUrl;

	@ApiOperation(value="Search for patients that match the parameters.")
	@RequestMapping(value = "/search", method = RequestMethod.POST)
	public @ResponseBody Query searchPatients(@RequestBody PatientSearch patientSearchTerms) throws JsonProcessingException {
		Query returnQuery;
		if(patientSearchTerms.getPatientName() != null
				&& patientSearchTerms.getDob() != null && patientSearchTerms.getPatientName().getFamilyName() != null
				&& patientSearchTerms.getPatientName().getGivenName() != null && patientSearchTerms.getGender() != null){
			
			MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
			ObjectMapper mapper = new ObjectMapper();

			JWTAuthenticatedUser jwtUser = (JWTAuthenticatedUser) SecurityContextHolder.getContext().getAuthentication();
			if(jwtUser == null){
				logger.error("Could not find a logged in user. ");
				throw new AccessDeniedException("Only logged in users can search for patients.");
			}else{
				headers.add("User", mapper.writeValueAsString(jwtUser));
				System.out.println(mapper.writeValueAsString(jwtUser));
				HttpEntity<PatientSearch> request = new HttpEntity<PatientSearch>(patientSearchTerms, headers);
				RestTemplate query = new RestTemplate();
				returnQuery = query.postForObject(brokerUrl + "/search", request, Query.class);
				logger.info("Request sent to broker from services REST.");
			}
		}else{
			throw new PatientSearchTermsException();
		}
		return returnQuery;
	}

	// get all patients from the logged in users ACF
	@ApiOperation(value="get all patients from the logged in users ACF.")
	@RequestMapping(value = "/patients", method = RequestMethod.GET)
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
	
	@ApiOperation(value="Delete the specified patient and all associated documents.")
	@RequestMapping(value="/patients/{patientId}/delete", method = RequestMethod.POST)
	public Void deletePatient(@PathVariable(value="patientId") Long patientId) throws JsonProcessingException {

		RestTemplate query = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		ObjectMapper mapper = new ObjectMapper();

		JWTAuthenticatedUser jwtUser = (JWTAuthenticatedUser) SecurityContextHolder.getContext().getAuthentication();
		HttpEntity<Void> response = null;
		if(jwtUser == null){
			logger.error("Could not find a logged in user. ");
		}else{
			headers.add("User", mapper.writeValueAsString(jwtUser));
			HttpEntity<Void> entity = new HttpEntity<Void>(headers);
			response = query.exchange(brokerUrl + "/patients/" + patientId + "/delete", HttpMethod.POST, entity, Void.class);
			logger.info("Request sent to broker from services REST.");
		}
		
		return response.getBody();
	}
}