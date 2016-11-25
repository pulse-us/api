package gov.ca.emsa.pulse.cten.domain;

import java.util.ArrayList;
import java.util.List;

public class Endpoint {
	private List<EndpointResource> resource;
	private String fullUrl;
	private Search search;
	
	public Endpoint() {
		resource = new ArrayList<EndpointResource>();
	}
	
	public List<EndpointResource> getResource() {
		return resource;
	}
	public void setResource(List<EndpointResource> resource) {
		this.resource = resource;
	}
	public String getFullUrl() {
		return fullUrl;
	}
	public void setFullUrl(String fullUrl) {
		this.fullUrl = fullUrl;
	}
	public Search getSearch() {
		return search;
	}
	public void setSearch(Search search) {
		this.search = search;
	}
}
