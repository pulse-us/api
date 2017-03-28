package gov.ca.emsa.pulse.common.domain;


public class DocumentQuery {
	
	private String patientId;
	private String[] documentStatuses;
	
	public String getPatientId() {
		return patientId;
	}
	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}
	public String[] getDocumentStatuses() {
		return documentStatuses;
	}
	public void setDocumentStatuses(String[] documentStatuses) {
		this.documentStatuses = documentStatuses;
	}
	
	

}
