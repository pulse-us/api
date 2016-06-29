package gov.ca.emsa.pulse.service;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
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
	public void createACF(@RequestBody AlternateCareFacility acf) {

		String patientUrl = "http://localhost:8090/acfs/create";
		RestTemplate query = new RestTemplate();
		UserAndACF uacf = new UserAndACF();
		uacf.setAcf(acf);

		Authentication auth =  SecurityContextHolder.getContext().getAuthentication();
		User user = new User();
		if(auth != null){
			user.setName(auth.getName());
			uacf.setUser(user);
		}else{
			logger.error("Could not find a logged in user. ");
		}

		query.postForObject(patientUrl, uacf, AlternateCareFacility.class);
	}

	// get all acfs
	@RequestMapping(value = "/acfs")
	public ArrayList<AlternateCareFacility> getACFs() {

		String patientUrl = "http://localhost:8090/acfs";
		RestTemplate query = new RestTemplate();

		Authentication auth =  SecurityContextHolder.getContext().getAuthentication();
		User user = new User();
		if(auth != null){
			user.setName(auth.getName());
		}else{
			logger.error("Could not find a logged in user. ");
		}

		AlternateCareFacility[] acfs = query.postForObject(patientUrl, user, AlternateCareFacility[].class);
		ArrayList<AlternateCareFacility> acfList = new ArrayList<AlternateCareFacility>(Arrays.asList(acfs));

		return acfList;
	}

	// get acf by its id
	@RequestMapping(value = "/acfs/{id}")
	public AlternateCareFacility getACFById(@PathVariable Long id) {

		String patientUrl = "http://localhost:8090/acfs/" + id;
		RestTemplate query = new RestTemplate();

		Authentication auth =  SecurityContextHolder.getContext().getAuthentication();
		User user = new User();
		if(auth != null){
			user.setName(auth.getName());
		}else{
			logger.error("Could not find a logged in user. ");
		}

		AlternateCareFacility acf = query.postForObject(patientUrl, user, AlternateCareFacility.class);

		return acf;
	}
}
