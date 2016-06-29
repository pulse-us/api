package gov.ca.emsa.pulse.service;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import org.springframework.web.bind.annotation.RequestMethod;

@RestController
public class AlternateCareFacilityService {
	
	private static final Logger logger = LogManager.getLogger(PatientService.class);

	// get all patients from the logged in users ACF
	@RequestMapping(value = "/acfs/create", method = RequestMethod.POST)
	public void createtACF(@RequestBody AlternateCareFacility acf) {

		String patientUrl = "http://localhost:8090/acfs/create";
		RestTemplate query = new RestTemplate();
		UserAndACF uacf = new UserAndACF();
		uacf.setAcf(acf);

		Authentication auth =  SecurityContextHolder.getContext().getAuthentication();
		User user = new User();
		if(auth != null){
			user.setName(auth.getName());
			user.setAcf("ACF1");
			uacf.setUser(user);
		}else{
			logger.error("Could not find a logged in user. ");
		}

		query.postForObject(patientUrl, uacf, AlternateCareFacility.class);
	}
}
