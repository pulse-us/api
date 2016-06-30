package gov.ca.emsa.pulse.broker.domain;

import java.util.List;

public class CreatePatientRequest {
	private List<Long> patientRecordIds;
	private Patient patient;
	public List<Long> getPatientRecordIds() {
		return patientRecordIds;
	}
	public void setPatientRecordIds(List<Long> patientRecordIds) {
		this.patientRecordIds = patientRecordIds;
	}
	public Patient getPatient() {
		return patient;
	}
	public void setPatient(Patient patient) {
		this.patient = patient;
	}
}
