package gov.ca.emsa.pulse.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.opensaml.xml.io.MarshallingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import gov.ca.emsa.pulse.broker.domain.Patient;
import gov.ca.emsa.pulse.broker.domain.Query;
import gov.ca.emsa.pulse.broker.domain.User;
import gov.ca.emsa.pulse.broker.dto.AddressDTO;
import gov.ca.emsa.pulse.broker.dto.DtoToDomainConverter;
import gov.ca.emsa.pulse.broker.dto.PatientRecordDTO;
import gov.ca.emsa.pulse.broker.dto.QueryDTO;
import gov.ca.emsa.pulse.broker.manager.PatientManager;
import gov.ca.emsa.pulse.broker.saml.SAMLInput;
import gov.ca.emsa.pulse.broker.saml.SamlGenerator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "patients")
@RestController
@RequestMapping("/patients")
public class PatientService {
	private static final Logger logger = LogManager.getLogger(PatientService.class);

	@Autowired private PatientManager patientManager;
	
	public PatientService() {
	}
	
		
	@ApiOperation(value="Get the stored results of the query")
	@RequestMapping("/query/{queryId}")
	public List<Patient> getPatientsForQuery(@PathVariable("queryId") Long queryId) {
		List<PatientRecordDTO> queryResults = patientManager.getPatientsByQuery(queryId);
		List<Patient> results = new ArrayList<Patient>(queryResults.size());
        for(PatientRecordDTO qr : queryResults) {
        	Patient pat = DtoToDomainConverter.convert(qr);
        	results.add(pat);
        }
        
        return results;
	}
}
