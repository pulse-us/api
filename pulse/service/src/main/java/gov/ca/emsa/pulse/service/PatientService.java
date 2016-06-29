package gov.ca.emsa.pulse.service;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import io.swagger.annotations.ApiOperation;

@RestController
public class PatientService implements EnvironmentAware{
	private static final Logger logger = LogManager.getLogger(PatientService.class);
	@Autowired private Environment env;

	@ApiOperation(value="Search for patients that match the parameters.")
	@RequestMapping(value = "/search", method = RequestMethod.POST)
	public void searchPatients(@RequestBody PatientSearchTerms patientSearchTerms) {

		String patientUrl = env.getProperty("brokerPatientUrl").trim();
		
		MultiValueMap<String,Object> parameters = new LinkedMultiValueMap<String,Object>();

		if(!StringUtils.isEmpty(patientSearchTerms.getFirstName())) {
			parameters.add("firstName", patientSearchTerms.getFirstName());
		}
		if(!StringUtils.isEmpty(patientSearchTerms.getLastName())) {
			parameters.add("lastName", patientSearchTerms.getLastName());
		}
		if(!StringUtils.isEmpty(patientSearchTerms.getDob())) {
			parameters.add("dob", patientSearchTerms.getDob());
		}
		if(!StringUtils.isEmpty(patientSearchTerms.getSsn())) {
			parameters.add("ssn", patientSearchTerms.getSsn());
		}
		if(!StringUtils.isEmpty(patientSearchTerms.getGender())) {
			parameters.add("gender", patientSearchTerms.getGender());
		}
		if(!StringUtils.isEmpty(patientSearchTerms.getZipcode())) {
			parameters.add("zip", patientSearchTerms.getZipcode());
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
		query.postForObject(patientUrl, user, Query.class, parameters);
		
	}

	// get all patients from the logged in users ACF
	@RequestMapping(value = "/patients")
	public Query getAllPatientsAtACF() {

		String patientUrl = "http://localhost:8090/patients";
		RestTemplate query = new RestTemplate();

		Authentication auth =  SecurityContextHolder.getContext().getAuthentication();
		User user = new User();
		if(auth != null){
			user.setName(auth.getName());
			user.setAcf("ACF1");
		}else{
			logger.error("Could not find a logged in user. ");
		}

		Query queryRet = query.postForObject(patientUrl, user, Query.class);

		return queryRet;
	}
	@Override
	public void setEnvironment(Environment environment) {
		// TODO Auto-generated method stub

	}
}
