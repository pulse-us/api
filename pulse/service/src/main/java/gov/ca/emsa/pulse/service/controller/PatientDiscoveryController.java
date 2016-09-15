package gov.ca.emsa.pulse.service.controller;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.xml.soap.SOAPException;

import gov.ca.emsa.pulse.auth.user.JWTAuthenticatedUser;
import gov.ca.emsa.pulse.common.domain.PatientSearch;
import gov.ca.emsa.pulse.common.domain.Query;
import gov.ca.emsa.pulse.common.domain.QueryOrganization;
import gov.ca.emsa.pulse.service.EHealthQueryConsumerService;
import gov.ca.emsa.pulse.service.PatientSearchService;
import gov.ca.emsa.pulse.service.PatientService;
import gov.ca.emsa.pulse.service.SOAPToJSONService;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.hl7.v3.PRPAIN201305UV02;
import org.opensaml.common.SAMLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class PatientDiscoveryController {
	private static final Logger logger = LogManager.getLogger(PatientDiscoveryController.class);

	@Value("${brokerUrl}")
	private String brokerUrl;
	@Value("${samlServiceUrl}")
	private String samlServiceUrl;
	@Value("${server.port}")
	private String port;

	@Autowired EHealthQueryConsumerService consumerService;
	@Autowired SOAPToJSONService converter;
	@Autowired PatientSearchService pss;
	private ExecutorService executor = Executors.newFixedThreadPool(100);

	@RequestMapping(value = "/patientDiscovery", method = RequestMethod.POST, consumes ={"application/xml"}, produces = {"application/xml"})
	public String patientDiscovery(@RequestBody String request){
		PRPAIN201305UV02 requestObj = null;
		try{
			requestObj = consumerService.unMarshallPatientDiscoveryRequestObject(request);
		}catch(SAMLException e){
			return consumerService.createSOAPFault();
		} catch (SOAPException e) {
			logger.error(e);
		}
		PatientSearch patientSearch = converter.convertToPatientSearch(requestObj);

		pss.searchForPatientWithTerms(patientSearch);
		
		Future<List<QueryOrganization>> future = executor.submit(pss);
		try {
			System.out.println(future.get());
		} catch (InterruptedException | ExecutionException e) {
			logger.error(e);
		}
		return null;
	}
}
