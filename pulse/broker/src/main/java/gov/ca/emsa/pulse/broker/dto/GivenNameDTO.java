package gov.ca.emsa.pulse.broker.dto;

import gov.ca.emsa.pulse.broker.entity.GivenNameEntity;


public class GivenNameDTO {
	
	private Long id;
	
	private String givenName;
	
	private Long PatientRecordNameId;
	
	public GivenNameDTO(){
		
	}
	
	public GivenNameDTO(GivenNameEntity entity){
		this.id = entity.getId();
		this.givenName = entity.getGivenName();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getGivenName() {
		return givenName;
	}

	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}

	public Long getPatientRecordNameId() {
		return PatientRecordNameId;
	}

	public void setPatientRecordNameId(Long PatientRecordNameId) {
		this.PatientRecordNameId = PatientRecordNameId;
	}

}
