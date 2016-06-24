package gov.ca.emsa.pulse.broker.domain;

import gov.ca.emsa.pulse.broker.dto.AlternateCareFacilityDTO;

public class AlternateCareFacility {
	private Long id;
	private String name;
	
	public AlternateCareFacility() {}
	public AlternateCareFacility(AlternateCareFacilityDTO dto) {
		this.id = dto.getId();
		this.name = dto.getName();
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
}
