package gov.ca.emsa.pulse.common.domain;

public class Document {
	// these documents need metadata
	private String id;
	private String name;
	private Boolean cached;
	private Long orgMapId;
	private Patient patient;
	
	public Document() {}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getOrgMapId() {
		return orgMapId;
	}
	public void setOrgMapId(Long orgMapId) {
		this.orgMapId = orgMapId;
	}

	public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	public Boolean getCached() {
		return cached;
	}

	public void setCached(Boolean cached) {
		this.cached = cached;
	}
	
}
