package gov.ca.emsa.pulse.service;

import gov.ca.emsa.pulse.common.domain.Document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;

public class DocumentQueryService extends EHealthQueryService{
	
	public DocumentQueryService(){
		super.setAuthorizationHeader();
	}
	
	public List<Document> queryForDocuments(RestTemplate restTemplate, String patientId){
		HttpEntity<Document[]> entity = new HttpEntity<Document[]>(this.getHeaders());
		HttpEntity<Document[]> response = restTemplate.exchange("http://localhost:" + this.getPort() + "/patients/{id}/documents", HttpMethod.GET, entity, Document[].class);
		return new ArrayList<Document>(Arrays.asList(response.getBody()));
	}
}
