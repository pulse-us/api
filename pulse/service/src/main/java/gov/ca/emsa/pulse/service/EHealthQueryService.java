package gov.ca.emsa.pulse.service;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class EHealthQueryService {
	private static final Logger logger = LogManager.getLogger(EHealthQueryService.class);
	
	@Value("${server.port}")
	private String port;

	@Value("${samlServiceUrl}")
	private String samlServiceUrl;

	private MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
	
	private RestTemplate restTemplate;
	
	public void setAuthorizationHeader(){
		ObjectMapper mapper = new ObjectMapper();
		HttpEntity<String> jwtResponse = null;

		HttpHeaders jwtHeader = new HttpHeaders();
		jwtHeader.setContentType(MediaType.TEXT_XML);
		HttpEntity<String> jwtRequest = new HttpEntity<String>(jwtHeader);
		jwtResponse = restTemplate.exchange(samlServiceUrl + "/jwt", HttpMethod.GET, jwtRequest , String.class);
		String jwt = jwtResponse.getBody();

		try {
			headers.add("Authorization", mapper.writeValueAsString(jwt).replaceAll("\"", ""));
		} catch (JsonProcessingException e) {
			logger.error(e);
		}
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getSamlServiceUrl() {
		return samlServiceUrl;
	}

	public void setSamlServiceUrl(String samlServiceUrl) {
		this.samlServiceUrl = samlServiceUrl;
	}

	public MultiValueMap<String, String> getHeaders() {
		return headers;
	}

	public void setHeaders(MultiValueMap<String, String> headers) {
		this.headers = headers;
	}

	public RestTemplate getRestTemplate() {
		return restTemplate;
	}

	public void setRestTemplate(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}
	
}
