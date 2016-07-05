package gov.ca.emsa.pulse.broker.domain;

import gov.ca.emsa.pulse.broker.dto.AlternateCareFacilityDTO;

public class AlternateCareFacility {
	private Long id;
	private String name;
	private String phoneNumber;
	private Address address;
	
	public AlternateCareFacility() {}
	public AlternateCareFacility(AlternateCareFacilityDTO dto) {
		this.id = dto.getId();
		this.name = dto.getName();
		this.phoneNumber = dto.getPhoneNumber();
		
		if(dto.getAddress() != null) {
			this.address = new Address(dto.getAddress());
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
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
}
