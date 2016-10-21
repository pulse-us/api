package gov.ca.emsa.pulse.service.controller;

import gov.ca.emsa.pulse.common.domain.DocumentRetrieve;
import gov.ca.emsa.pulse.common.domain.DocumentWrapper;
import gov.ca.emsa.pulse.common.soap.JSONToSOAPService;
import gov.ca.emsa.pulse.common.soap.SOAPToJSONService;
import gov.ca.emsa.pulse.service.DocumentRetrieveService;
import gov.ca.emsa.pulse.service.EHealthQueryConsumerService;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;

import java.util.List;

import javax.xml.bind.JAXBException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.opensaml.common.SAMLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class DocumentSetRetrieveController {
	private static final Logger logger = LogManager.getLogger(DocumentSetRetrieveController.class);
	@Autowired private ResourceLoader resourceLoader;
	private static final String RESOURCE_FILE_NAME = "ValidDocumentSetRetrieveResponse.xml";

	@Autowired EHealthQueryConsumerService consumerService;
	@Autowired SOAPToJSONService SOAPService;
	@Autowired DocumentRetrieveService docRetrieveService;
	@Autowired JSONToSOAPService JSONService;

	@RequestMapping(value = "/retrieveDocumentSet", method = RequestMethod.POST, produces = MediaType.APPLICATION_XML_VALUE)
	public String documentRequest(@RequestBody String request) {
		RetrieveDocumentSetRequestType requestObj;
		RestTemplate restTemplate = new RestTemplate();
		try {
			requestObj = consumerService.unMarshallDocumentSetRetrieveRequestObject(request);
		} catch (SAMLException e1) {
			return consumerService.createSOAPFault();
		}
		logger.info("Document set Request object: " + requestObj.toString());
		DocumentRetrieve docRetrieve = SOAPService.convertToDocumentSetRequest(requestObj);
		
		List<DocumentWrapper> docs = docRetrieveService.retrieveDocuments(restTemplate, docRetrieve, "");
		
		RetrieveDocumentSetResponseType responseObj = JSONService.convertDocumentSetToSOAPResponse(docs);
		logger.info("Document set Response object: " + responseObj.toString());
		String response = null;
		try {
			response = consumerService.marshallDocumentSetResponse(responseObj);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		logger.info("Document set Response string: " + response);
		return response;
	}
}
