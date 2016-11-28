package gov.ca.emsa.pulse.cten.domain;

import java.util.ArrayList;
import java.util.List;

public class OrganizationResource {
	private ResourceMetadata meta;
	private String active;
	private ResourceType type;
	private String name;
	private List<String> alias;
	private List<EndpointMetadata> endpoint;
	private List<Address> address;
	private String resourceType;
	private String id;
	
	public OrganizationResource() {
		alias = new ArrayList<String>();
		endpoint = new ArrayList<EndpointMetadata>();
		address = new ArrayList<Address>();
	}
	
	public ResourceMetadata getMeta() {
		return meta;
	}
	public void setMeta(ResourceMetadata meta) {
		this.meta = meta;
	}
	public String getActive() {
		return active;
	}
	public void setActive(String active) {
		this.active = active;
	}
	public ResourceType getType() {
		return type;
	}
	public void setType(ResourceType type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<String> getAlias() {
		return alias;
	}
	public void setAlias(List<String> alias) {
		this.alias = alias;
	}
	public List<EndpointMetadata> getEndpoint() {
		return endpoint;
	}
	public void setEndpoint(List<EndpointMetadata> endpoint) {
		this.endpoint = endpoint;
	}
	public List<Address> getAddress() {
		return address;
	}
	public void setAddress(List<Address> address) {
		this.address = address;
	}
	public String getResourceType() {
		return resourceType;
	}
	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
}
