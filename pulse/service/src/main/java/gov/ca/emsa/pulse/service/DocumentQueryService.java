package gov.ca.emsa.pulse.service;

import gov.ca.emsa.pulse.common.domain.Document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class DocumentQueryService extends EHealthQueryService{
	
	public List<Document> queryForDocuments(RestTemplate restTemplate, String patientId){
		this.setRestTemplate(restTemplate);
		super.setAuthorizationHeader();
		HttpEntity<Document[]> entity = new HttpEntity<Document[]>(this.getHeaders());
		HttpEntity<Document[]> response = restTemplate.exchange("http://localhost:" + this.getPort() + "/patients/" + patientId + "/documents", HttpMethod.GET, entity, Document[].class);
		return new ArrayList<Document>(Arrays.asList(response.getBody()));
	}
}
