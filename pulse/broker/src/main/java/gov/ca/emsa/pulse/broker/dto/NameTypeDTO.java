package gov.ca.emsa.pulse.broker.dto;

import gov.ca.emsa.pulse.broker.entity.NameTypeEntity;

public class NameTypeDTO {
	
	private Long id;
	
	private String code;
	
	private String description;
	
	public NameTypeDTO(NameTypeEntity entity){
		this.id = entity.getId();
		this.code = entity.getCode();
		this.description = entity.getDescription();
	}

	public NameTypeDTO() {
		// TODO Auto-generated constructor stub
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
