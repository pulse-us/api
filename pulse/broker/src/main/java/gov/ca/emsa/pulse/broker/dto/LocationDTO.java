package gov.ca.emsa.pulse.broker.dto;

import gov.ca.emsa.pulse.broker.entity.LocationAddressLineEntity;
import gov.ca.emsa.pulse.broker.entity.LocationEndpointEntity;
import gov.ca.emsa.pulse.broker.entity.LocationEndpointMimeTypeEntity;
import gov.ca.emsa.pulse.broker.entity.LocationEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LocationDTO extends AddressableDTO {
	
	private Long id;
	private String externalId;
	private LocationStatusDTO status;
	private String parentOrgName;
	private String name;
	private String description;
	private String type;
	private Date externalLastUpdateDate;
	private Date creationDate;
	private Date lastModifiedDate;
	
	private List<LocationEndpointDTO> endpoints;
	
	public LocationDTO(){
		super();
		this.endpoints = new ArrayList<LocationEndpointDTO>();
	}
	
	public LocationDTO(LocationEntity entity){
		this();
		
		this.id = entity.getId();
		this.externalId = entity.getExternalId();
		if(entity.getLocationStatus() != null) {
			this.status = new LocationStatusDTO(entity.getLocationStatus());
		} else {
			this.status = new LocationStatusDTO();
			this.status.setId(entity.getLocationStatusId());
		}
		this.parentOrgName = entity.getParentOrganizationName();
		this.name = entity.getName();
		this.description = entity.getDescription();
		this.type = entity.getType();
		if(entity.getLines() != null) {
			for(LocationAddressLineEntity lineEntity : entity.getLines()) {
				AddressLineDTO line = new AddressLineDTO();
				line.setId(lineEntity.getId());
				line.setLine(lineEntity.getLine());
				line.setCreationDate(lineEntity.getCreationDate());
				line.setLastModifiedDate(line.getLastModifiedDate());
				this.lines.add(line);
			}
		}
		this.city = entity.getCity();
		this.state = entity.getState();
		this.zipcode = entity.getZipcode();
		this.country = entity.getCountry();
		this.externalLastUpdateDate = entity.getExternalLastUpdatedDate();
		this.creationDate = entity.getCreationDate();
		this.lastModifiedDate = entity.getLastModifiedDate();
		
		if(entity.getEndpoints() != null) {
			for(LocationEndpointEntity endpointEntity : entity.getEndpoints()) {
				LocationEndpointDTO endpointDto = new LocationEndpointDTO(endpointEntity);
				this.endpoints.add(endpointDto);
			}
		}
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name = name;
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

	public LocationStatusDTO getStatus() {
		return status;
	}

	public void setStatus(LocationStatusDTO status) {
		this.status = status;
	}

	public String getParentOrgName() {
		return parentOrgName;
	}

	public void setParentOrgName(String parentOrgName) {
		this.parentOrgName = parentOrgName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getExternalLastUpdateDate() {
		return externalLastUpdateDate;
	}

	public void setExternalLastUpdateDate(Date externalLastUpdateDate) {
		this.externalLastUpdateDate = externalLastUpdateDate;
	}

	public List<LocationEndpointDTO> getEndpoints() {
		return endpoints;
	}

	public void setEndpoints(List<LocationEndpointDTO> endpoints) {
		this.endpoints = endpoints;
	}
}
