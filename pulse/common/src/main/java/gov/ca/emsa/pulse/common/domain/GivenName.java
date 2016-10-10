package gov.ca.emsa.pulse.common.domain;

public class GivenName {
	
	private Long id;
	
	private String givenName;
	
	private Long patientNameId;

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

	public Long getPatientNameId() {
		return patientNameId;
	}

	public void setPatientNameId(Long patientNameId) {
		this.patientNameId = patientNameId;
	}
}
