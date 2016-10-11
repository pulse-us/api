package gov.ca.emsa.pulse.broker.dto;

import gov.ca.emsa.pulse.broker.entity.AddressEntity;

import java.util.Date;

public class AddressDTO {

	private Long id;
	private String streetLineOne;
	private String streetLineTwo;
	private String city;
	private String state;
	private String zipcode;
	private String country;
	private Date creationDate;
	private Date lastModifiedDate;
	
	public AddressDTO(){}
	
	public AddressDTO(AddressEntity entity)
	{
		if(entity != null) {
			this.id = entity.getId();
			this.streetLineOne = entity.getStreetLineOne();
			this.streetLineTwo = entity.getStreetLineTwo();
			this.city = entity.getCity();
			this.state = entity.getState();
			this.zipcode = entity.getZipcode();
			this.country = entity.getCountry();
			this.creationDate = entity.getCreationDate();
			this.lastModifiedDate = entity.getLastModifiedDate();
		}
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getStreetLineOne() {
		return streetLineOne;
	}

	public void setStreetLineOne(String streetLineOne) {
		this.streetLineOne = streetLineOne;
	}

	public String getStreetLineTwo() {
		return streetLineTwo;
	}

	public void setStreetLineTwo(String streetLineTwo) {
		this.streetLineTwo = streetLineTwo;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
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

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}
}
