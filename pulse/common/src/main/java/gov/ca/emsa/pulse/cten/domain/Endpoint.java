package gov.ca.emsa.pulse.cten.domain;

public class Endpoint {
	private EndpointResource resource;
	private String fullUrl;
	private Search search;
	
	public Endpoint() {
	}
	
	public EndpointResource getResource() {
		return resource;
	}
	public void setResource(EndpointResource resource) {
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
