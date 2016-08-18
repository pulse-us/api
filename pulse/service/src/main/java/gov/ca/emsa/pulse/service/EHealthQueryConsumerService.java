package gov.ca.emsa.pulse.service;

import gov.ca.emsa.pulse.auth.user.JWTAuthenticatedUser;
import gov.ca.emsa.pulse.common.domain.PatientSearch;
import gov.ca.emsa.pulse.common.domain.Query;
import gov.ca.emsa.pulse.xcpd.XcpdUtils;
import gov.ca.emsa.pulse.xcpd.prpa.cap.qbp.ParameterList;
import gov.ca.emsa.pulse.xcpd.soap.DiscoveryRequestSoapEnvelope;
import gov.ca.emsa.pulse.xcpd.soap.DiscoveryResponseSoapEnvelope;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class EHealthQueryConsumerService {
	
	private static final Logger logger = LogManager.getLogger(EHealthQueryConsumerService.class);
	@Value("${brokerUrl}")
	private String brokerUrl;
	
	@RequestMapping(value = "/patientDiscovery", method = RequestMethod.POST, produces = MediaType.APPLICATION_XML_VALUE)
	public DiscoveryResponseSoapEnvelope patientDiscovery(@RequestBody DiscoveryRequestSoapEnvelope dr) throws JsonProcessingException{
		PatientSearch patientSearchTerms = new PatientSearch();
		ParameterList parameterList = dr.sBody.PRPA_IN201305UV02.controlActProcess.getQueryByParameter().parameterList;
		patientSearchTerms.setDob(parameterList.getLivingSubjectBirthTime().value.value);
		patientSearchTerms.setGender(parameterList.getLivingSubjectAdministrativeGender().value.code);
		patientSearchTerms.setFirstName(parameterList.getLivingSubjectName().getValue().given);
		patientSearchTerms.setLastName(parameterList.getLivingSubjectName().getValue().family);
		
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
		ObjectMapper mapper = new ObjectMapper();

		JWTAuthenticatedUser jwtUser = (JWTAuthenticatedUser) SecurityContextHolder.getContext().getAuthentication();
		if(jwtUser == null){
			logger.error("Could not find a logged in user. ");
		}else{
			headers.add("User", mapper.writeValueAsString(jwtUser));
			HttpEntity<PatientSearch> request = new HttpEntity<PatientSearch>(patientSearchTerms, headers);
			RestTemplate query = new RestTemplate();
			query.postForObject(brokerUrl + "/search", request, Query.class);
			logger.info("Request sent to broker from services REST.");
		}
		
		return XcpdUtils.generateQueryResponse(patientSearchTerms.getFirstName(), patientSearchTerms.getLastName());
		
	}

}
