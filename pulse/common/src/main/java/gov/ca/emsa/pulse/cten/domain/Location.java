package gov.ca.emsa.pulse.cten.domain;

import java.util.ArrayList;
import java.util.List;

public class Location {
	private List<LocationResource> resource;
	private String fullUrl;
	private Search search;
	
	public Location() {
		resource = new ArrayList<LocationResource>();
	}
	
	public List<LocationResource> getResource() {
		return resource;
	}
	public void setResource(List<LocationResource> resource) {
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
