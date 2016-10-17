package gov.ca.emsa.pulse.common.domain;

import java.util.ArrayList;
import java.util.List;

public class Address {
	private Long id;
	private List<AddressLine> lines;
	
	//not deleting this fields yet only because they are still used
	//in the patient and patient record objects. when those address
	//fields get moved to the new format, these two street lines should be deleted!!
	private String street1;
	private String street2;
	
	
	private String city;
	private String state;
	private String zipcode;
	private String country;
	
	public Address(){
		this.lines = new ArrayList<AddressLine>();
	}
	
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

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public List<AddressLine> getLines() {
		return lines;
	}

	public void setLines(List<AddressLine> lines) {
		this.lines = lines;
	}

	public String getStreet1() {
		return street1;
	}

	public void setStreet1(String street1) {
		this.street1 = street1;
	}

	public String getStreet2() {
		return street2;
	}

	public void setStreet2(String street2) {
		this.street2 = street2;
	}
}
