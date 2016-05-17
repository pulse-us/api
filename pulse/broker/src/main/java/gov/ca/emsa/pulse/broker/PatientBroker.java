package gov.ca.emsa.pulse.broker;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import gov.ca.emsa.pulse.broker.domain.Patient;

@Component
public class PatientBroker {
	
	public List<Patient> queryPatients(String firstName, String lastName) {
		
		//TODO: query the cache 
		//TODO: if no cache hit
			//get the list of organizations from directory services
			//for each organization, pass in the patient query
		
		RestTemplate restTemplate = new RestTemplate();
		List<Patient> patients = restTemplate.getForObject("http://localhost:8080/mock/patients", List.class);
		
		//TODO: cache the patients returned with some identifier so we can 
		//pull them out of the cache again
		return patients;
	}
}
