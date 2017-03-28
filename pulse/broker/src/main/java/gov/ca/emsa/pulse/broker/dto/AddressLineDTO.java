package gov.ca.emsa.pulse.broker.dto;

import java.util.Date;

import gov.ca.emsa.pulse.broker.entity.AlternateCareFacilityAddressLineEntity;
import gov.ca.emsa.pulse.broker.entity.LocationAddressLineEntity;

public class AddressLineDTO {

	private Long id;
	private String line;
	private Date creationDate;
	private Date lastModifiedDate;
	
	public AddressLineDTO(){}

	public AddressLineDTO(AlternateCareFacilityAddressLineEntity entity) {
		this.id = entity.getId();
		this.line = entity.getLine();
		this.creationDate = entity.getCreationDate();
		this.lastModifiedDate = entity.getLastModifiedDate();
	}
	
	public AddressLineDTO(LocationAddressLineEntity entity) {
		this.id = entity.getId();
		this.line = entity.getLine();
		this.creationDate = entity.getCreationDate();
		this.lastModifiedDate = entity.getLastModifiedDate();
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLine() {
		return line;
	}

	public void setLine(String line) {
		this.line = line;
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
