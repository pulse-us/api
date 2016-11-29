package gov.ca.emsa.pulse.broker.adapter;

import gov.ca.emsa.pulse.broker.adapter.service.EHealthQueryProducerService;
import gov.ca.emsa.pulse.broker.dao.NameTypeDAO;
import gov.ca.emsa.pulse.broker.dto.AuditEventDTO;
import gov.ca.emsa.pulse.broker.dto.AuditHumanRequestorDTO;
import gov.ca.emsa.pulse.broker.dto.AuditQueryParametersDTO;
import gov.ca.emsa.pulse.broker.dto.AuditRequestDestinationDTO;
import gov.ca.emsa.pulse.broker.dto.AuditRequestSourceDTO;
import gov.ca.emsa.pulse.broker.dto.AuditSourceDTO;
import gov.ca.emsa.pulse.broker.dto.DocumentDTO;
import gov.ca.emsa.pulse.broker.dto.DomainToDtoConverter;
import gov.ca.emsa.pulse.broker.dto.DtoToDomainConverter;
import gov.ca.emsa.pulse.broker.dto.NameTypeDTO;
import gov.ca.emsa.pulse.broker.dto.OrganizationDTO;
import gov.ca.emsa.pulse.broker.dto.PatientOrganizationMapDTO;
import gov.ca.emsa.pulse.broker.dto.PatientRecordDTO;
import gov.ca.emsa.pulse.broker.dto.SearchResultConverter;
import gov.ca.emsa.pulse.broker.manager.AuditEventManager;
import gov.ca.emsa.pulse.broker.saml.SAMLInput;
import gov.ca.emsa.pulse.common.domain.Document;
import gov.ca.emsa.pulse.common.domain.NameType;
import gov.ca.emsa.pulse.common.domain.Patient;
import gov.ca.emsa.pulse.common.domain.PatientRecord;
import gov.ca.emsa.pulse.common.domain.PatientSearch;
import gov.ca.emsa.pulse.common.soap.JSONToSOAPService;
import gov.ca.emsa.pulse.common.soap.SOAPToJSONService;
import gov.ca.emsa.pulse.service.AuditUtil;
import gov.ca.emsa.pulse.service.UserUtil;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType.DocumentResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import javax.activation.DataHandler;
import javax.xml.bind.JAXBException;
import javax.xml.soap.SOAPException;

import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;

import org.apache.commons.io.IOUtils;
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
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

@Component
public class EHealthAdapter implements Adapter {
	private static final Logger logger = LogManager.getLogger(EHealthAdapter.class);

	@Autowired JSONToSOAPService jsonConverterService;
	@Autowired SOAPToJSONService soapConverterService;
	@Autowired EHealthQueryProducerService queryProducer;
	@Autowired NameTypeDAO nameTypeDao;
	@Autowired AuditEventManager auditManager;

	@Override
	public List<PatientRecordDTO> queryPatients(OrganizationDTO org, PatientSearch toSearch, SAMLInput samlInput) throws Exception {
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
			logger.info("Querying " + postUrl + " with request " + request);
			String homeCommunityId = requestBody.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0).getRoot();
			createAuditEventIG(jsonConverterService.getQueryByParameter(requestBody).toString(), homeCommunityId);
			searchResults = restTemplate.postForObject(postUrl, request, String.class);
		} catch(Exception ex) {
			logger.error("Exception when querying " + postUrl, ex);
			throw ex;
		}

		List<PatientRecordDTO> records = new ArrayList<PatientRecordDTO>();
		if(!StringUtils.isEmpty(searchResults)) {
			try {
				PRPAIN201306UV02 resultObj = queryProducer.unMarshallPatientDiscoveryResponseObject(searchResults);
				List<PatientRecord> patientResults = soapConverterService.convertToPatientRecords(resultObj);
				for(int i = 0; i < patientResults.size(); i++) {
					PatientRecordDTO record = DomainToDtoConverter.convertToPatientRecord(patientResults.get(i));
					for(int j = 0; j < record.getPatientRecordName().size(); j++){
						NameTypeDTO nameType = nameTypeDao.getByCode("L");
						record.getPatientRecordName().get(j).setNameType(nameType);
					}
					records.add(record);
				}
			} catch(SAMLException | SOAPException ex) {
				logger.error("Exception unmarshalling patient discovery response", ex);
			}
		}

		return records;
	}

	@Override
	public List<DocumentDTO> queryDocuments(OrganizationDTO org, PatientOrganizationMapDTO toSearch, SAMLInput samlInput) {
		Patient patientToSearch = new Patient();
		toSearch.setOrgPatientRecordId(toSearch.getOrgPatientRecordId());
		AdhocQueryRequest requestBody = jsonConverterService.convertToDocumentRequest(patientToSearch);
		String requestBodyXml = null;
		try {
			requestBodyXml = queryProducer.marshallDocumentQueryRequest(samlInput, requestBody);
		} catch(JAXBException ex) {
			logger.error(ex.getMessage(), ex);
		}

		String postUrl = org.getEndpointUrl() + "/documentQuery";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_XML);   
		HttpEntity<String> request = new HttpEntity<String>(requestBodyXml, headers);

		String searchResults = null;
		try {
			RestTemplate restTemplate = new RestTemplate();
			logger.info("Querying " + postUrl + " with request " + request);
			searchResults = restTemplate.postForObject(postUrl, request, String.class);
		} catch(Exception ex) {
			logger.error("Exception when querying " + postUrl, ex);
			throw ex;
		}

		List<DocumentDTO> records = new ArrayList<DocumentDTO>();
		if(!StringUtils.isEmpty(searchResults)) {
			try {
				AdhocQueryResponse resultObj = queryProducer.unMarshallDocumentQueryResponseObject(searchResults);
				List<Document> documentResults = soapConverterService.convertToDocumentQueryResponse(resultObj);
				for(int i = 0; i < documentResults.size(); i++) {
					DocumentDTO record = DomainToDtoConverter.convert(documentResults.get(i));
					records.add(record);
				}
			} catch(SAMLException | SOAPException ex) {
				logger.error("Exception unmarshalling document discovery response", ex);
			}
		}
		return records;
	}

	/**
	 * get all of the document contents available from the given organization
	 * @param orgMap
	 * @param documents
	 * @return
	 */
	@Override
	public void retrieveDocumentsContents(OrganizationDTO org, List<DocumentDTO> documents, SAMLInput samlInput) {
		List<Document> docsToSearch = new ArrayList<Document>();
		for(DocumentDTO docDto : documents) {
			Document doc = DtoToDomainConverter.convert(docDto);
			docsToSearch.add(doc);
		}
		RetrieveDocumentSetRequestType requestBody = jsonConverterService.convertToRetrieveDocumentSetRequest(docsToSearch);
		String requestBodyXml = null;
		try {
			requestBodyXml = queryProducer.marshallDocumentSetRequest(samlInput, requestBody);
		} catch(JAXBException ex) {
			logger.error(ex.getMessage(), ex);
		}

		String postUrl = org.getEndpointUrl() + "/retrieveDocumentSet";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_XML);   
		HttpEntity<String> request = new HttpEntity<String>(requestBodyXml, headers);

		String searchResults = null;
		try {
			RestTemplate restTemplate = new RestTemplate();
			logger.info("Querying " + postUrl + " with request " + request);
			searchResults = restTemplate.postForObject(postUrl, request, String.class);
		} catch(Exception ex) {
			logger.error("Exception when querying " + postUrl, ex);
			throw ex;
		}

		if(!StringUtils.isEmpty(searchResults)) {
			try {
				RetrieveDocumentSetResponseType resultObj = queryProducer.unMarshallDocumentSetRetrieveResponseObject(searchResults);
				List<DocumentResponse> documentResponses = soapConverterService.convertToDocumentSetResponse(resultObj);
				for(DocumentResponse docResponse : documentResponses) {
					//find the matching DocumentDTO that we sent in
					DocumentDTO matchingDto = null;
					for(int i = 0; i < documents.size() && matchingDto == null; i++) {
						DocumentDTO currDto = documents.get(i);
						if(currDto.getRepositoryUniqueId().equals(docResponse.getRepositoryUniqueId()) && 
								currDto.getHomeCommunityId().equals(docResponse.getHomeCommunityId()) &&
								currDto.getDocumentUniqueId().equals(docResponse.getDocumentUniqueId())) {
							matchingDto = currDto;
						}
					}

					if(matchingDto != null) {
						//read the binary document data from this DocumentResponse
						DataHandler dataHandler = docResponse.getDocument();
						InputStream in = null;
						try {
							in = dataHandler.getDataSource().getInputStream();
							StringWriter writer = new StringWriter();
							IOUtils.copy(in, writer, Charset.forName("UTF-8"));
							String dataStr = base64DecodeMessage(writer.toString());
							logger.debug("Converted binary to " + dataStr);
							matchingDto.setContents(dataStr.getBytes());
						} catch(IOException e) {
							e.printStackTrace();
						} finally {
							try {
								if(in != null) { in.close(); }
							} catch(Exception ignore) {}
						}
					}
				}
			} catch(SAMLException ex) {
				logger.error("Exception unmarshalling document retrieve response", ex);
			}
		}
	}

	private String base64DecodeMessage(String encodedMessage){       
		byte[] decoded = Base64.getDecoder().decode(encodedMessage);
		String decodedMessage = new String(decoded, Charset.forName("UTF-8"));
		return decodedMessage;
	}
	
	private byte[] base64EncodeMessage(String queryByParameters){
		byte[] bytesEncoded = Base64.getEncoder().encode(queryByParameters.getBytes());
		return bytesEncoded;
	}

	// create an audit event for an initiating gateway audit message
	private void createAuditEventIG(String queryByParameter, String homeCommunityId) throws UnknownHostException{
		AuditRequestSourceDTO auditRequestSourceDTO = AuditUtil.createAuditRequestSource(InetAddress.getLocalHost().toString() + "/patientDiscovery", // not sure about this
				ManagementFactory.getRuntimeMXBean().getName().split("@")[0],
				"", // this is optional
				true, // this is optional
				"EV(110153, DCM, “Source”)",
				"2",
				InetAddress.getLocalHost().toString());
		ArrayList<AuditHumanRequestorDTO> auditHumanRequestorDTO = AuditUtil.createAuditHumanRequestor(UserUtil.getCurrentUser().getFirstName()
				+ " " + UserUtil.getCurrentUser().getLastName() + " " + UserUtil.getCurrentUser().getEmail(), // the identity of the human that initiated the transaction
				"", // optional
				"", // optional
				false, // optional
				"", // optional
				"", // optional
				"");// optional
		AuditRequestDestinationDTO auditRequestDestinationDTO = AuditUtil.createAuditRequestDestination("https://www.someihe.com/patientDiscovery",
				"", // optional
				"", // optional
				false, 
				"EV(110152, DCM, “Destination”)", 
				"2", 
				"The IP of the request destination ");
		AuditQueryParametersDTO auditQueryParametersDTO = AuditUtil.createAuditQueryParameters(2, 
				24, 
				"", // optional 
				"EV(“ITI-55, “IHE Transactions”, “Cross Gateway Patient Discovery”)", 
				"", // optional 
				"", // optional 
				"", // optional
				"", // the QueryByParameter segment of the query, base64 encoded.
				""); // The value of “ihe:homeCommunityID” as the value of the attribute type and the value of the homeCommunityID as the value of the attribute value.
		AuditSourceDTO auditSouceDTO = AuditUtil.createAuditSource("", // optional 
				"", // optional 
				""); // optional
		AuditEventDTO auditEventDTO = new AuditEventDTO();
		auditEventDTO.setEventId("EV(110112, DCM, “Query”)");
		auditEventDTO.setEventActionCode("E");
		auditEventDTO.setEventDateTime(new Date().toString());
		auditEventDTO.setEventOutcomeIndicator(""); // TODO need to figure out this value
		auditEventDTO.setEventTypeCode("EV(“ITI-55”, “IHE Transactions”, “Cross Gateway Patient Discovery”)");
		auditEventDTO.setAuditRequestSource(auditRequestSourceDTO);
		auditEventDTO.setAuditHumanRequestors(auditHumanRequestorDTO);
		auditEventDTO.setAuditRequestDestination(auditRequestDestinationDTO);
		auditEventDTO.setAuditQueryParameters(auditQueryParametersDTO);
		auditEventDTO.setAuditSource(auditSouceDTO);
		auditManager.addAuditEventEntryIG(auditEventDTO);
	}
}
