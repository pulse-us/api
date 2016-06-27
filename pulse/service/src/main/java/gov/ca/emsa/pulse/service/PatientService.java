package gov.ca.emsa.pulse.service;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import gov.ca.emsa.pulse.service.Patient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
@Api(value = "search")
@RestController
@RequestMapping("/search")
public class PatientService implements EnvironmentAware{
	private static final Logger logger = LogManager.getLogger(PatientService.class);
	@Autowired private Environment env;

	@ApiOperation(value="Search for patients that match the parameters.")
	@RequestMapping("/patient")
	public Query searchPatients(
			@RequestParam(value="firstName", defaultValue="") String firstName,
			@RequestParam(value="lastName", defaultValue="") String lastName,
    		@RequestParam(value="dob", defaultValue="") String dob,
    		@RequestParam(value="ssn", defaultValue="") String ssn,
    		@RequestParam(value="gender", defaultValue="") String gender,
    		@RequestParam(value="zipcode", defaultValue="") String zip) {

		String patientUrl = "http://localhost:8090/patients";
				//env.getProperty("brokerPatientUrl").trim();
		
		MultiValueMap<String,Object> parameters = new LinkedMultiValueMap<String,Object>();

		if(!StringUtils.isEmpty(firstName)) {
			parameters.add("firstName", firstName);
		}
		if(!StringUtils.isEmpty(lastName)) {
			parameters.add("lastName", lastName);
		}
		if(!StringUtils.isEmpty(dob)) {
			parameters.add("dob", dob);
		}
		if(!StringUtils.isEmpty(ssn)) {
			parameters.add("ssn", ssn);
		}
		if(!StringUtils.isEmpty(gender)) {
			parameters.add("gender", gender);
		}
		if(!StringUtils.isEmpty(zip)) {
			parameters.add("zip", zip);
		}
		
		Authentication auth =  SecurityContextHolder.getContext().getAuthentication();
		User user = new User();
		if(auth != null){
			user.setName(auth.getName());
			parameters.add("user", user);
		}else{
			logger.error("Could not find a logged in user. ");
		}

		RestTemplate query = new RestTemplate();
		Query patientQueryResults = query.postForObject(patientUrl, user, Query.class, parameters);
		
		return patientQueryResults;
	}
	
	@RequestMapping("patient/query/{queryId}")
	public List<Patient> getPatientsForQuery(@PathVariable("queryId") Long queryId) {
		
		String patientUrl = "http://localhost:8090/patients/query/" + queryId;
		RestTemplate query = new RestTemplate();
		Patient[] patientQueryResults = query.getForObject(patientUrl, Patient[].class);
		ArrayList<Patient> patientResults = new ArrayList<Patient>(Arrays.asList(patientQueryResults));
        
        return patientResults;
	}

	@Override
	public void setEnvironment(Environment environment) {
		// TODO Auto-generated method stub
		
	}
}
