package gov.ca.emsa.pulse.cten.domain;

import java.util.ArrayList;
import java.util.List;

public class LocationResource {
	private String status;
	private String name;
	private String mode;
	private String description;
	private ResourceType type;
	private ResourceType physicalType;
	private Address address;
	private ManagingOrganization managingOrganization;
	private ResourceMetadata meta;
	private List<EndpointMetadata> endpoint;
	
	public LocationResource() {
		endpoint = new ArrayList<EndpointMetadata>();
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public ResourceType getType() {
		return type;
	}
	public void setType(ResourceType type) {
		this.type = type;
	}
	public ResourceType getPhysicalType() {
		return physicalType;
	}
	public void setPhysicalType(ResourceType physicalType) {
		this.physicalType = physicalType;
	}
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
	}
	public ManagingOrganization getManagingOrganization() {
		return managingOrganization;
	}
	public void setManagingOrganization(ManagingOrganization managingOrganization) {
		this.managingOrganization = managingOrganization;
	}
	public ResourceMetadata getMeta() {
		return meta;
	}
	public void setMeta(ResourceMetadata meta) {
		this.meta = meta;
	}
	public List<EndpointMetadata> getEndpoint() {
		return endpoint;
	}
	public void setEndpoint(List<EndpointMetadata> endpoint) {
		this.endpoint = endpoint;
	}
}
