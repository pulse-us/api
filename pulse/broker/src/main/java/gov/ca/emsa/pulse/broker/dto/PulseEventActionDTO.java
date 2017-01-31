package gov.ca.emsa.pulse.broker.dto;

import java.util.Date;

import gov.ca.emsa.pulse.broker.entity.AuditRequestSourceEntity;
import gov.ca.emsa.pulse.broker.entity.PulseEventActionEntity;

public class PulseEventActionDTO {
	
	private Long id;
	
	private String username;
	
	private Date actionTStamp;
	
	private String actionJson;
	
	private Long pulseEventActionCodeId;
	
	private Date creationDate;
	
	private Date lastModifiedDate;
	
	public PulseEventActionDTO(){
		
	}
	
	public PulseEventActionDTO(PulseEventActionEntity entity){
		this.id = entity.getId();
		this.username = entity.getUsername();
		this.actionTStamp = entity.getActionTStamp();
		this.actionJson = entity.getActionJson();
		this.pulseEventActionCodeId = entity.getPulseEventActionCodeId();
		this.creationDate = entity.getCreationDate();
		this.lastModifiedDate = entity.getLastModifiedDate();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Date getActionTStamp() {
		return actionTStamp;
	}

	public void setActionTStamp(Date actionTStamp) {
		this.actionTStamp = actionTStamp;
	}

	public String getActionJson() {
		return actionJson;
	}

	public void setActionJson(String actionJson) {
		this.actionJson = actionJson;
	}

	public Long getPulseEventActionCodeId() {
		return pulseEventActionCodeId;
	}

	public void setPulseEventActionCodeId(Long long1) {
		this.pulseEventActionCodeId = long1;
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
