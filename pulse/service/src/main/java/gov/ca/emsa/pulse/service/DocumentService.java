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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import gov.ca.emsa.pulse.service.Document;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "search")
@RestController
@RequestMapping("/search")
public class DocumentService {
	private static final Logger logger = LogManager.getLogger(PatientService.class);
	@Autowired private Environment env;

	@ApiOperation(value="Search Documents for a Document.")
	@RequestMapping("/documents")
	public List<Document> searchDocuments(
			@RequestParam(value="patientId", required=true) String patientId) throws Exception {
		
		String url = "http://localhost:8090/documents";
				//env.getProperty("brokerDocumentUrl").trim();
		
		MultiValueMap<String,Object> parameters = new LinkedMultiValueMap<String,Object>();
		Authentication auth =  SecurityContextHolder.getContext().getAuthentication();
		User user = new User();
		if(auth != null){
			user.setName(auth.getName());
			parameters.add("user", user);
		}else{
			logger.error("Could not find a logged in user. ");
		}
		parameters.add("patientId", patientId);
		
		RestTemplate query = new RestTemplate();
		Document[] queryResults = query.postForObject(url, user, Document[].class, parameters);
		ArrayList<Document> DocumentList = new ArrayList<Document>(Arrays.asList(queryResults));

		return DocumentList;
	}

	@ApiOperation(value="Retrieve a specific Document from an organization.")
	@RequestMapping("document/{DocumentId}")
	public String getDocumentContents(@PathVariable("documentId") Long documentId,
			@RequestParam(value="cacheOnly", required= false, defaultValue="true") Boolean cacheOnly) {
		
		String url = "http://localhost:8090/documents";
		
		MultiValueMap<String,Object> parameters = new LinkedMultiValueMap<String,Object>();
		Authentication auth =  SecurityContextHolder.getContext().getAuthentication();
		User user = new User();
		if(auth != null){
			user.setName(auth.getName());
			parameters.add("user", user);
		}else{
			logger.error("Could not find a logged in user. ");
		}
		parameters.add("patientId", documentId);
		
		RestTemplate query = new RestTemplate();
		String queryResults = query.postForObject(url, user, String.class, parameters);
		
		return queryResults;
	}
}
