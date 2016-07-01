package gov.ca.emsa.pulse.broker.dto;

import java.util.Date;

import gov.ca.emsa.pulse.broker.entity.AlternateCareFacilityEntity;

public class AlternateCareFacilityDTO {

	private Long id;
	private String name;
	private AddressDTO address;
	private Date lastReadDate;
	private Date creationDate;
	private Date lastModifiedDate;
	
	public AlternateCareFacilityDTO(){}
	
	public AlternateCareFacilityDTO(AlternateCareFacilityEntity entity)
	{
		if(entity != null) {
			this.id = entity.getId();
			this.name = entity.getName();
			this.lastReadDate = entity.getLastReadDate();
			this.creationDate = entity.getCreationDate();
			this.lastModifiedDate = entity.getLastModifiedDate();
			
			if(entity.getAddress() != null) {
				this.address = new AddressDTO(entity.getAddress());
			}
		}
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getLastReadDate() {
		return lastReadDate;
	}

	public void setLastReadDate(Date lastReadDate) {
		this.lastReadDate = lastReadDate;
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

	public AddressDTO getAddress() {
		return address;
	}

	public void setAddress(AddressDTO address) {
		this.address = address;
	}
}
