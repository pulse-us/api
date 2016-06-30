package gov.ca.emsa.pulse.broker.domain;

import java.util.Date;

import gov.ca.emsa.pulse.broker.dto.AddressDTO;

public class Address {
	private Long id;
	private String streetLine1;
	private String streetLine2;
	private String city;
	private String state;
	private String zipcode;
	
	public Address(){}
	
	public Address(AddressDTO dto) {
		this.id = dto.getId();
		this.streetLine1 = dto.getStreetLineOne();
		this.streetLine2 = dto.getStreetLineTwo();
		this.city = dto.getCity();
		this.state = dto.getState();
		this.zipcode = dto.getZipcode();
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public String getStreetLine1() {
		return streetLine1;
	}
	public void setStreetLine1(String addressLine1) {
		this.streetLine1 = addressLine1;
	}
	public String getStreetLine2() {
		return streetLine2;
	}
	public void setStreetLine2(String addressLine2) {
		this.streetLine2 = addressLine2;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getZipcode() {
		return zipcode;
	}
	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
}
