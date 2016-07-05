package gov.ca.emsa.pulse.service;

import java.util.Date;


public class Address {
	private Long id;
	private String street1;
	private String street2;
	private String city;
	private String state;
	private String zipcode;
	private String country;
	
	public Address(){}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
