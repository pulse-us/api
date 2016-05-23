package gov.ca.emsa.pulse.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gov.ca.emsa.pulse.broker.domain.Patient;
import gov.ca.emsa.pulse.broker.dto.DtoToDomainConverter;
import gov.ca.emsa.pulse.broker.dto.PatientDTO;
import gov.ca.emsa.pulse.broker.manager.PatientManager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "patients")
@RestController
@RequestMapping("/patients")
public class PatientService {
	@Autowired PatientManager patientManager;
	
		@ApiOperation(value="Query all organizations for patients.")
		@RequestMapping("")
	    public List<Patient> searchPatients(
	    		@RequestParam(value="firstName", defaultValue="") String firstName,
	    		@RequestParam(value="lastName", defaultValue="") String lastName) {
	        List<PatientDTO> queryResults = patientManager.queryPatients(firstName, lastName);
	        
	        List<Patient> results = new ArrayList<Patient>(queryResults.size());
	        for(PatientDTO qr : queryResults) {
	        	Patient pat = DtoToDomainConverter.convert(qr);
	        	results.add(pat);
	        }
	        
	        return results;
	    }
}
