package gov.ca.emsa.pulse.broker.domain;

import gov.ca.emsa.pulse.broker.dto.AlternateCareFacilityDTO;

public class AlternateCareFacility {
	private Long id;
	private String name;
	private Address address;
	
	public AlternateCareFacility() {}
	public AlternateCareFacility(AlternateCareFacilityDTO dto) {
		this.id = dto.getId();
		this.name = dto.getName();
		
		if(dto.getAddress() != null) {
			this.address = new Address();
			this.address.setStreetLine1(dto.getAddress().getStreetLineOne());
			this.address.setStreetLine2(dto.getAddress().getStreetLineTwo());
			this.address.setCity(dto.getAddress().getCity());
			this.address.setState(dto.getAddress().getState());
			this.address.setZipcode(dto.getAddress().getZipcode());
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
}
