package gov.ca.emsa.pulse.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.opensaml.xml.io.MarshallingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import gov.ca.emsa.pulse.broker.domain.Query;
import gov.ca.emsa.pulse.broker.domain.User;
import gov.ca.emsa.pulse.broker.dto.AddressDTO;
import gov.ca.emsa.pulse.broker.dto.PatientRecordDTO;
import gov.ca.emsa.pulse.broker.dto.QueryDTO;
import gov.ca.emsa.pulse.broker.manager.QueryManager;
import gov.ca.emsa.pulse.broker.saml.SAMLInput;
import gov.ca.emsa.pulse.broker.saml.SamlGenerator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "search")
@RestController
@RequestMapping("/search")
public class SearchService {
	
	private static final Logger logger = LogManager.getLogger(SearchService.class);
	@Autowired private SamlGenerator samlGenerator;
	@Autowired private QueryManager searchManager;
	public static String dobFormat = "yyyy-MM-dd";
	private DateFormat formatter;
	
	public SearchService() {
		formatter = new SimpleDateFormat(dobFormat);
	}
	
	@ApiOperation(value="Query all organizations for patients. This runs asynchronously and returns a query object"
			+ "which can later be used to get the results.")
	@RequestMapping(method = RequestMethod.POST,
		produces="application/json; charset=utf-8",consumes="application/json")
    public @ResponseBody Query searchPatients(
    		@RequestParam(value="firstName", defaultValue="") String firstName,
    		@RequestParam(value="lastName", defaultValue="") String lastName,
    		@RequestParam(value="dob", defaultValue="") String dob,
    		@RequestParam(value="ssn", defaultValue="") String ssn,
    		@RequestParam(value="gender", defaultValue="") String gender,
    		@RequestParam(value="zipcode", defaultValue="") String zip) throws JsonProcessingException {
		
		User user = UserUtil.getCurrentUser();

		SAMLInput input = new SAMLInput();
		input.setStrIssuer("https://idp.dhv.gov");
		input.setStrNameID("UserBrianLindsey");
		input.setStrNameQualifier("My Website");
		input.setSessionId("abcdedf1234567");
		
		HashMap<String, String> customAttributes = new HashMap<String,String>();
		customAttributes.put("RequesterFirstName", user.getName());
		customAttributes.put("RequestReason", "Patient is bleeding.");
		customAttributes.put("PatientFirstName", firstName);
		customAttributes.put("PatientLastName", lastName);
		customAttributes.put("PatientDOB", dob);
		customAttributes.put("PatientGender", gender);
		customAttributes.put("PatientHomeZip", zip);
		customAttributes.put("PatientSSN", ssn);
		
		input.setAttributes(customAttributes);
		
		String samlMessage = null;
		
		try {
			samlMessage = samlGenerator.createSAML(input);
		} catch (MarshallingException e) {
			logger.error("Could not create SAML from input " + input, e);
		}
		
		PatientRecordDTO toSearch = new PatientRecordDTO();
		toSearch.setFirstName(firstName);
		toSearch.setLastName(lastName);
		if(!StringUtils.isEmpty(dob)) {
			try {
				Date dateOfBirth = formatter.parse(dob);
				toSearch.setDateOfBirth(dateOfBirth);
			} catch(ParseException ex) {
				logger.error("Could not parse date " + dob, ex);
			}
		}
		toSearch.setGender(gender);
		toSearch.setSsn(ssn);
		AddressDTO toSearchAddress = new AddressDTO();
		toSearchAddress.setZipcode(zip);
		toSearch.setAddress(toSearchAddress);
       QueryDTO initiatedQuery = searchManager.queryForPatientRecords(samlMessage, toSearch, user);
       return new Query(initiatedQuery);
    }
}
