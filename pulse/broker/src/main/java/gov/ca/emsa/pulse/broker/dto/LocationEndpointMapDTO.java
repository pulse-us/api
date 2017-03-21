package gov.ca.emsa.pulse.broker.dto;

import gov.ca.emsa.pulse.broker.entity.LocationAddressLineEntity;
import gov.ca.emsa.pulse.broker.entity.LocationEndpointMapEntity;
import gov.ca.emsa.pulse.broker.entity.EndpointEntity;
import gov.ca.emsa.pulse.broker.entity.LocationEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LocationEndpointMapDTO {
	
	private Long id;
	private Long locationId;
	private Long endpointId;
	private Date creationDate;
	private Date lastModifiedDate;
	
	public LocationEndpointMapDTO(){
		super();
	}
	
	public LocationEndpointMapDTO(LocationEndpointMapEntity entity){
		this();
		
		this.id = entity.getId();
		this.locationId = entity.getLocationId();
		this.endpointId = entity.getEndpointId();
		this.creationDate = entity.getCreationDate();
		this.lastModifiedDate = entity.getLastModifiedDate();
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getLocationId() {
		return locationId;
	}

	public void setLocationId(Long locationId) {
		this.locationId = locationId;
	}

	public Long getEndpointId() {
		return endpointId;
	}

	public void setEndpointId(Long endpointId) {
		this.endpointId = endpointId;
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
}
