package gov.ca.emsa.pulse.service.controller;

import java.util.List;

import javax.xml.bind.JAXBException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.opensaml.common.SAMLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import gov.ca.emsa.pulse.common.domain.Document;
import gov.ca.emsa.pulse.common.domain.DocumentQuery;
import gov.ca.emsa.pulse.common.soap.JSONToSOAPService;
import gov.ca.emsa.pulse.common.soap.SOAPToJSONService;
import gov.ca.emsa.pulse.service.DocumentQueryService;
import gov.ca.emsa.pulse.service.EHealthQueryConsumerService;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;

@RestController
public class DocumentQueryController {
	private static final Logger logger = LogManager.getLogger(DocumentQueryController.class);
	private static final String RESOURCE_FILE_NAME = "ValidDocumentQueryResponse.xml";
	
	@Value("${server.port}")
	private String port;
	@Autowired private ResourceLoader resourceLoader;
	@Autowired EHealthQueryConsumerService consumerService;
	@Autowired SOAPToJSONService SOAPService;
	@Autowired DocumentQueryService docQueryService;
	@Autowired JSONToSOAPService JSONService;

	@RequestMapping(value = "/documentQuery", method = RequestMethod.GET, produces = MediaType.APPLICATION_XML_VALUE)
	public String queryRequest(@RequestBody String request) {
		RestTemplate restTemplate = new RestTemplate();
		AdhocQueryRequest requestObj;
		try{
			requestObj = consumerService.unMarshallDocumentQueryRequestObject(request);
		}catch(SAMLException e){
			return consumerService.createSOAPFault();
		}
		
		DocumentQuery docQuery = SOAPService.convertToDocumentQuery(requestObj);
		
		// these documents need to have metadata
		List<Document> docs = docQueryService.queryForDocuments(restTemplate, docQuery.getPatientId());
		
		AdhocQueryResponse responseObj = JSONService.convertDocumentListToSOAPResponse(docs, docQuery.getPatientId());
		
		String response = null;
		try {
			response = consumerService.marshallDocumentQueryResponse(responseObj);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return response;
	}
}
