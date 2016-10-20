package gov.ca.emsa.pulse.common.domain;

public class GivenName {
	
	private Long id;
	
	private String givenName;
	
	private Long PatientRecordNameId;

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
