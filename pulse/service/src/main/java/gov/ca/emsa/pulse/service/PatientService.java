package gov.ca.emsa.pulse.service;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import gov.ca.emsa.pulse.auth.user.JWTAuthenticatedUser;
import gov.ca.emsa.pulse.service.Patient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "search")
@RestController
@RequestMapping("/search")
public class PatientService implements EnvironmentAware{
	@Autowired private Environment env;

	@ApiOperation(value="Search for patients that match the parameters.")
	@RequestMapping("/patient")
	public List<Patient> searchPatients(
			@RequestParam(value="firstName", defaultValue="") String firstName,
			@RequestParam(value="lastName", defaultValue="") String lastName) {

		String url = env.getProperty("brokerPatientUrl").trim();
		
		MultiValueMap<String,Object> parameters = new LinkedMultiValueMap<String,Object>();

		if(!StringUtils.isEmpty(firstName)) {
			parameters.add("firstName", firstName);
		}
		if(!StringUtils.isEmpty(lastName)) {
			parameters.add("lastName", lastName);
		}
		
		JWTAuthenticatedUser user = (JWTAuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		if(user != null){
			parameters.add("user", user);
		}

		RestTemplate query = new RestTemplate();
		Patient[] queryResults = query.postForObject(url, parameters, Patient[].class);
		ArrayList<Patient> patientList = new ArrayList<Patient>(Arrays.asList(queryResults));

		return patientList;
	}

	@Override
	public void setEnvironment(Environment environment) {
		this.env = environment;
		
	}
}
