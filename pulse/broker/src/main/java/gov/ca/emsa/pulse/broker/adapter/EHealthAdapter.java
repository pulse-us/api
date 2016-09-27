package gov.ca.emsa.pulse.broker.adapter;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.soap.SOAPException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;
import org.opensaml.common.SAMLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import gov.ca.emsa.pulse.broker.adapter.service.EHealthQueryProducerService;
import gov.ca.emsa.pulse.broker.dto.OrganizationDTO;
import gov.ca.emsa.pulse.broker.dto.PatientOrganizationMapDTO;
import gov.ca.emsa.pulse.broker.dto.PatientRecordDTO;
import gov.ca.emsa.pulse.broker.dto.SearchResultConverter;
import gov.ca.emsa.pulse.broker.saml.SAMLInput;
import gov.ca.emsa.pulse.common.domain.Document;
import gov.ca.emsa.pulse.common.domain.Patient;
import gov.ca.emsa.pulse.common.domain.PatientSearch;
import gov.ca.emsa.pulse.common.soap.JSONToSOAPService;
import gov.ca.emsa.pulse.common.soap.SOAPToJSONService;

@Component
public class EHealthAdapter implements Adapter {
	private static final Logger logger = LogManager.getLogger(EHealthAdapter.class);
	
	@Autowired JSONToSOAPService jsonConverterService;
	@Autowired SOAPToJSONService soapConverterService;
	@Autowired EHealthQueryProducerService queryProducer;
	
	@Override
	public List<PatientRecordDTO> queryPatients(OrganizationDTO org, PatientSearch toSearch, SAMLInput samlInput) {
		PRPAIN201305UV02 requestBody = jsonConverterService.convertFromPatientSearch(toSearch);
		String requestBodyXml = null;
		try {
			requestBodyXml = queryProducer.marshallPatientDiscoveryRequest(samlInput, requestBody);
		} catch(JAXBException ex) {
			logger.error(ex.getMessage(), ex);
		}

		String postUrl = org.getEndpointUrl() + "/patientDiscovery";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_XML);   
		HttpEntity<String> request = new HttpEntity<String>(requestBodyXml, headers);

		String searchResults = null;
		try {
			RestTemplate restTemplate = new RestTemplate();
			searchResults = restTemplate.postForObject(postUrl, request, String.class);
		} catch(Exception ex) {
			logger.error("Exception when querying " + postUrl, ex);
			throw ex;
		}
		
		List<PatientRecordDTO> records = new ArrayList<PatientRecordDTO>();
		if(!StringUtils.isEmpty(searchResults)) {
			try {
				PRPAIN201306UV02 resultObj = queryProducer.unMarshallPatientDiscoveryResponseObject(searchResults);
				List<Patient> patientResults = soapConverterService.convertToPatients(resultObj);
				for(int i = 0; i < patientResults.size(); i++) {
					PatientRecordDTO record = SearchResultConverter.convertToPatientRecord(patientResults.get(i));
					records.add(record);
				}
			} catch(SAMLException | SOAPException ex) {
				logger.error("Exception unmarshalling patient discovery response", ex);
			}
		}
		
		return records;
	}

	@Override
	public Document[] queryDocuments(OrganizationDTO org, PatientOrganizationMapDTO toSearch, String samlMessage) {
		String postUrl = org.getEndpointUrl() + "/documents";
		MultiValueMap<String,String> parameters = new LinkedMultiValueMap<String,String>();
		parameters.add("patientId", toSearch.getOrgPatientId());
		parameters.add("samlMessage", samlMessage);
		RestTemplate restTemplate = new RestTemplate();
		Document[] searchResults = null;
		try {
			searchResults = restTemplate.postForObject(postUrl, parameters, Document[].class);
		} catch(Exception ex) {
			logger.error("Exception when querying " + postUrl, ex);
			throw ex;
		}
		return searchResults;
	}
}
