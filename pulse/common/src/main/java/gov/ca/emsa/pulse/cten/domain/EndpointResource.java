package gov.ca.emsa.pulse.cten.domain;

import java.util.ArrayList;
import java.util.List;

public class EndpointResource {
	private String status;
	private ResourceMetadata meta;
	private String name;
	private String address;
	private String payloadFormat;
	private List<ResourceType> payloadType;
	private ResourceCoding connectionType;
	private String publicKey;
	private String resourceType;
	private String id;
	
	public EndpointResource() {
		payloadType = new ArrayList<ResourceType>();
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public ResourceMetadata getMeta() {
		return meta;
	}
	public void setMeta(ResourceMetadata meta) {
		this.meta = meta;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPayloadFormat() {
		return payloadFormat;
	}
	public void setPayloadFormat(String payloadFormat) {
		this.payloadFormat = payloadFormat;
	}
	public List<ResourceType> getPayloadType() {
		return payloadType;
	}
	public void setPayloadType(List<ResourceType> payloadType) {
		this.payloadType = payloadType;
	}
	public ResourceCoding getConnectionType() {
		return connectionType;
	}
	public void setConnectionType(ResourceCoding connectionType) {
		this.connectionType = connectionType;
	}
	public String getPublicKey() {
		return publicKey;
	}
	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
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
