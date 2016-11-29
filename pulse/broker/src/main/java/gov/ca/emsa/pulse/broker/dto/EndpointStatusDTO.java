package gov.ca.emsa.pulse.broker.dto;

import gov.ca.emsa.pulse.broker.entity.EndpointStatusEntity;

import java.util.Date;

public class EndpointStatusDTO {
	
	private Long id;
	private String name;
	private Date creationDate;
	private Date lastModifiedDate;
	
	public EndpointStatusDTO(){}
	
	public EndpointStatusDTO(EndpointStatusEntity status){
		this.id = status.getId();
		this.name = status.getName();
		this.creationDate = status.getCreationDate();
		this.lastModifiedDate = status.getLastModifiedDate();
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name = name;
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
