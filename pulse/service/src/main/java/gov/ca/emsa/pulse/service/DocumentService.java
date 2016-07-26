package gov.ca.emsa.pulse.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import gov.ca.emsa.pulse.common.domain.AlternateCareFacility;
import gov.ca.emsa.pulse.common.domain.Document;
import gov.ca.emsa.pulse.auth.user.JWTAuthenticatedUser;
import io.swagger.annotations.ApiOperation;

@RestController
public class DocumentService {
	private static final Logger logger = LogManager.getLogger(DocumentService.class);
	
	@Value("${brokerUrl}")
	private String brokerUrl;

	@ApiOperation(value="Search Documents for the given patient id.")
	@RequestMapping("/patients/{id}/documents")
	public List<Document> searchDocuments(@PathVariable Long id) throws Exception {

		RestTemplate query = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		ObjectMapper mapper = new ObjectMapper();

		JWTAuthenticatedUser jwtUser = (JWTAuthenticatedUser) SecurityContextHolder.getContext().getAuthentication();
		ArrayList<Document> docList = null;
		if(jwtUser == null){
			logger.error("Could not find a logged in user. ");
		}else{
			headers.set("User", mapper.writeValueAsString(jwtUser));
			HttpEntity<Document[]> entity = new HttpEntity<Document[]>(headers);
			HttpEntity<Document[]> response = query.exchange(brokerUrl + "/patients/" + id + "/documents", HttpMethod.GET, entity, Document[].class);
			logger.info("Request sent to broker from services REST.");
			docList = new ArrayList<Document>(Arrays.asList(response.getBody()));
		}

		return docList;
	}

	@ApiOperation(value="Retrieve a specific Document from an organization.")
	@RequestMapping("/patients/{patientId}/documents/{documentId}")
	public String getDocumentContents(@PathVariable("documentId") Long documentId,
			@PathVariable("patientId") Long patientId,
			@RequestParam(value="cacheOnly", required= false, defaultValue="true") Boolean cacheOnly) throws JsonProcessingException {

		RestTemplate query = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		ObjectMapper mapper = new ObjectMapper();

		JWTAuthenticatedUser jwtUser = (JWTAuthenticatedUser) SecurityContextHolder.getContext().getAuthentication();
		HttpEntity<String> response = null;
		if(jwtUser == null){
			logger.error("Could not find a logged in user. ");
		}else{

			headers.set("User", mapper.writeValueAsString(jwtUser));
			HttpEntity<Document> entity = new HttpEntity<Document>(headers);
			response = query.exchange(brokerUrl + "/patients/" + patientId + "/documents/" + documentId + "?cacheOnly=" + cacheOnly.toString(), HttpMethod.GET, entity, String.class);
			logger.info("Request sent to broker from services REST.");
		}

		return response.getBody();
	}
}
