package gov.ca.emsa.pulse.service;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

public class OrganizationService {
	
	private static final Logger logger = LogManager.getLogger(PatientService.class);
	
	// get all organizations
	@RequestMapping(value = "/organizations")
	public ArrayList<Organization> getACFs() {

		String patientUrl = "http://localhost:8090/organizations";
		RestTemplate query = new RestTemplate();

		Authentication auth =  SecurityContextHolder.getContext().getAuthentication();
		User user = new User();
		if(auth != null){
			user.setName(auth.getName());
		}else{
			logger.error("Could not find a logged in user. ");
		}

		Organization[] orgs = query.postForObject(patientUrl, user, Organization[].class);
		ArrayList<Organization> orgList = new ArrayList<Organization>(Arrays.asList(orgs));

		return orgList;
	}
}
