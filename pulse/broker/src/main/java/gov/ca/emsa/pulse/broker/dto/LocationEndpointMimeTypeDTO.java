package gov.ca.emsa.pulse.broker.dto;

import java.util.Date;

import gov.ca.emsa.pulse.broker.entity.LocationEndpointMimeTypeEntity;

public class LocationEndpointMimeTypeDTO {
	
	private Long id;
	private Long endpointId;
	private String mimeType;
	private Date creationDate;
	private Date lastModifiedDate;
		
	public LocationEndpointMimeTypeDTO(){
		super();
	}
	
	public LocationEndpointMimeTypeDTO(LocationEndpointMimeTypeEntity entity){
		this();
		
		this.id = entity.getId();
		this.endpointId = entity.getEndpointId();
		this.mimeType = entity.getMimeType();
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

	public Long getEndpointId() {
		return endpointId;
	}

	public void setEndpointId(Long endpointId) {
		this.endpointId = endpointId;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}
}
