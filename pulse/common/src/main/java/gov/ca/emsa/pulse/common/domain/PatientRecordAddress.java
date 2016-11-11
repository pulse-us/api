package gov.ca.emsa.pulse.common.domain;

import java.util.ArrayList;
import java.util.List;

public class PatientRecordAddress {
	private Long id;
	private List<String> lines;
	private String city;
	private String state;
	private String zipcode;
	
	public PatientRecordAddress(){
		this.lines = new ArrayList<String>();
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

	public void setLines(List<String> lines) {
		this.lines = lines;
	}

	public List<String> getLines() {
		return lines;
	}
}
