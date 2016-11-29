package gov.ca.emsa.pulse.broker.dto;

import java.util.Date;

import gov.ca.emsa.pulse.broker.entity.LocationEndpointEntity;

public class LocationEndpointDTO {
	
	private Long id;
	private String externalId;
	private EndpointTypeDTO endpointType;
	private EndpointStatusDTO endpointStatus;
	private String adapter;
	private String payloadFormat;
	private String payloadType;
	private String publicKey;
	private String url;
	private Date externalLastUpdateDate;
	private Date creationDate;
	private Date lastModifiedDate;
	
	public LocationEndpointDTO(){
	}
	
	public LocationEndpointDTO(LocationEndpointEntity entity){
		this();
		
		this.id = entity.getId();
		this.externalId = entity.getExternalId();
		if(entity.getEndpointType() != null) {
			this.endpointType = new EndpointTypeDTO(entity.getEndpointType());
		} else {
			this.endpointType = new EndpointTypeDTO();
			this.endpointType.setId(entity.getEndpointTypeId());
		}
		
		if(entity.getEndpointStatus() != null) {
			this.endpointStatus = new EndpointStatusDTO(entity.getEndpointStatus());
		} else {
			this.endpointStatus = new EndpointStatusDTO();
			this.endpointStatus.setId(entity.getEndpointStatusId());
		}
		this.adapter = entity.getAdapter();
		this.payloadFormat = entity.getPayloadFormat();
		this.payloadType = entity.getPayloadType();
		this.publicKey = entity.getPublicKey();
		this.url = entity.getUrl();
		this.externalLastUpdateDate = entity.getExternalLastUpdateDate();
		this.creationDate = entity.getCreationDate();
		this.lastModifiedDate = entity.getLastModifiedDate();
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

	public Date getExternalLastUpdateDate() {
		return externalLastUpdateDate;
	}

	public void setExternalLastUpdateDate(Date externalLastUpdateDate) {
		this.externalLastUpdateDate = externalLastUpdateDate;
	}

	public EndpointTypeDTO getEndpointType() {
		return endpointType;
	}

	public void setEndpointType(EndpointTypeDTO endpointType) {
		this.endpointType = endpointType;
	}

	public EndpointStatusDTO getEndpointStatus() {
		return endpointStatus;
	}

	public void setEndpointStatus(EndpointStatusDTO endpointStatus) {
		this.endpointStatus = endpointStatus;
	}

	public String getAdapter() {
		return adapter;
	}

	public void setAdapter(String adapter) {
		this.adapter = adapter;
	}

	public String getPayloadFormat() {
		return payloadFormat;
	}

	public void setPayloadFormat(String payloadFormat) {
		this.payloadFormat = payloadFormat;
	}

	public String getPayloadType() {
		return payloadType;
	}

	public void setPayloadType(String payloadType) {
		this.payloadType = payloadType;
	}

	public String getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
