package gov.ca.emsa.pulse.broker.domain;

import java.util.Date;

public class Address {
	private String id;
	private String streetLine1;
	private String streetLine2;
	private String city;
	private String state;
	private String zipcode;
	private String ssn;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
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
	public String getSsn() {
		return ssn;
	}
	public void setSsn(String ssn) {
		this.ssn = ssn;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
}
