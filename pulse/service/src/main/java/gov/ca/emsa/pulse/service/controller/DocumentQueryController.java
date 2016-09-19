package gov.ca.emsa.pulse.service.controller;

import gov.ca.emsa.pulse.common.domain.Document;
import gov.ca.emsa.pulse.common.domain.DocumentQuery;
import gov.ca.emsa.pulse.service.DocumentQueryService;
import gov.ca.emsa.pulse.service.EHealthQueryConsumerService;
import gov.ca.emsa.pulse.service.SOAPToJSONService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;

import org.opensaml.common.SAMLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

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
		
		List<Document> docs = docQueryService.queryForDocuments(restTemplate, docQuery.getPatientId());
		
		
	}
}
