package gov.ca.emsa.pulse.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import gov.ca.emsa.pulse.service.Document;
import io.swagger.annotations.ApiOperation;

@RestController
public class DocumentService {
	private static final Logger logger = LogManager.getLogger(PatientService.class);
	@Autowired private Environment env;

	@ApiOperation(value="Search Documents for the given patient id.")
	@RequestMapping("/patients/{id}/documents")
	public List<Document> searchDocuments(@PathVariable Long id) throws Exception {
		
		String url = "http://localhost:8090/patients/{id}/documents";
				//env.getProperty("brokerDocumentUrl").trim();
		
		Authentication auth =  SecurityContextHolder.getContext().getAuthentication();
		User user = new User();
		if(auth != null){
			user.setName(auth.getName());
		}else{
			logger.error("Could not find a logged in user. ");
		}
		
		RestTemplate query = new RestTemplate();
		Document[] queryResults = query.postForObject(url, user, Document[].class);
		ArrayList<Document> DocumentList = new ArrayList<Document>(Arrays.asList(queryResults));

		return DocumentList;
	}

	@ApiOperation(value="Retrieve a specific Document from an organization.")
	@RequestMapping("/patients/{id}/documents/{id}")
	public String getDocumentContents(@PathVariable("documentId") Long documentId,
			@PathVariable("patientId") Long patientId,
			@RequestParam(value="cacheOnly", required= false, defaultValue="true") Boolean cacheOnly) {
		
		String url = "http://localhost:8090/patients/{patientId}/documents/{documentId}?cacheOnly=" + cacheOnly.toString();
		
		Authentication auth =  SecurityContextHolder.getContext().getAuthentication();
		User user = new User();
		if(auth != null){
			user.setName(auth.getName());
		}else{
			logger.error("Could not find a logged in user. ");
		}
		
		RestTemplate query = new RestTemplate();
		String queryResults = query.postForObject(url, user, String.class);
		
		return queryResults;
	}
}
