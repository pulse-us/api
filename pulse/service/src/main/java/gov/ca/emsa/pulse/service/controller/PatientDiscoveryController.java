package gov.ca.emsa.pulse.service.controller;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.xml.bind.JAXBException;
import javax.xml.soap.SOAPException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201310UV02;
import org.opensaml.common.SAMLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import gov.ca.emsa.pulse.common.domain.PatientRecord;
import gov.ca.emsa.pulse.common.domain.PatientSearch;
import gov.ca.emsa.pulse.common.domain.QueryOrganization;
import gov.ca.emsa.pulse.common.soap.JSONToSOAPService;
import gov.ca.emsa.pulse.common.soap.SOAPToJSONService;
import gov.ca.emsa.pulse.service.EHealthQueryConsumerService;
import gov.ca.emsa.pulse.service.PatientSearchService;

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
	@Autowired SOAPToJSONService SOAPconverter;
	@Autowired PatientSearchService pss;
	@Autowired JSONToSOAPService JSONConverter;
	private ExecutorService executor = Executors.newFixedThreadPool(100);

	@RequestMapping(value = "/patientDiscovery", method = RequestMethod.POST, produces = MediaType.APPLICATION_XML_VALUE)
	public String patientDiscovery(@RequestBody String request){
		RestTemplate restTemplate = new RestTemplate();
		PRPAIN201305UV02 requestObj = null;
		try{
			requestObj = consumerService.unMarshallPatientDiscoveryRequestObject(request);
		}catch(SAMLException e){
			return consumerService.createSOAPFault();
		} catch (SOAPException e) {
			logger.error(e);
		}
		String creationTime = requestObj.getCreationTime().getValue();
		logger.info("Patient discovery Request object(" + creationTime + "): " + requestObj.toString());

		PatientSearch patientSearch = SOAPconverter.convertToPatientSearch(requestObj);
		
		logger.info("Patient discovery Patient search object(" + creationTime + "): " + patientSearch.toString());

		pss.searchForPatientWithTerms(restTemplate, patientSearch);
		
		Future<List<QueryOrganization>> future = executor.submit(pss);
		List<QueryOrganization> results = null;
		try {
			results = future.get();
		} catch (InterruptedException | ExecutionException e) {
			logger.error(e);
		}
		List<PatientRecord> patientRecords = results.get(0).getResults();
		for(QueryOrganization queryOrg: results){
			patientRecords.addAll(queryOrg.getResults());
		}
		PRPAIN201310UV02 responseObj = JSONConverter.convertPatientRecordListToSOAPResponse(patientRecords);
		logger.info("Patient discovery Response object(" + creationTime + "): " + responseObj.toString());
		String response = null;
		try {
			response = consumerService.marshallPatientDiscoveryResponse(responseObj);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		logger.info("Patient discovery Response string(" + creationTime + "): " + response);
		return response;
	}
}
