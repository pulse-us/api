package gov.ca.emsa.pulse.broker.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.StringUtils;

public class AddressableDTO {
	protected List<AddressLineDTO> lines;
	protected String city;
	protected String state;
	protected String zipcode;
	protected String country;
	
	public AddressableDTO() {
		this.lines = new ArrayList<AddressLineDTO>();
	}

	public List<AddressLineDTO> getLines() {
		return lines;
	}

	public void setLines(List<AddressLineDTO> lines) {
		this.lines = lines;
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

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
	
	public boolean hasAddressParts() {
		return (this.lines != null && this.lines.size() > 0) ||
				!StringUtils.isEmpty(this.city) ||
				!StringUtils.isEmpty(this.state) ||
				!StringUtils.isEmpty(this.country) ||
				!StringUtils.isEmpty(this.zipcode);
	}
}
