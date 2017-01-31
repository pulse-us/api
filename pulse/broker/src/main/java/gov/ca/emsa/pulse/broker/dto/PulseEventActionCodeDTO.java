package gov.ca.emsa.pulse.broker.dto;

import gov.ca.emsa.pulse.broker.entity.EventActionCodeEntity;
import gov.ca.emsa.pulse.broker.entity.PulseEventActionCodeEntity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

public class PulseEventActionCodeDTO {
	
	private Long id;
	
	private String code;
	
	private String description;
	
	private Date creationDate;
	
	private Date lastModifiedDate;
	
	public PulseEventActionCodeDTO(PulseEventActionCodeEntity entity) {
		super();
		this.id = entity.getId();
		this.code = entity.getCode();
		this.description = entity.getDescription();
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

	public void setName(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

}
