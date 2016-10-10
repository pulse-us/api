package gov.ca.emsa.pulse.common.domain;

public class PatientSearch {
	private PatientName patientName;
	private String dob;
	private String ssn;
	private String gender;
	private String zip;
	
	public PatientSearch(){
		patientName = new PatientName();
	}
	
	public String getDob() {
		return dob;
	}
	public void setDob(String dob) {
		this.dob = dob;
	}
	public String getSsn() {
		return ssn;
	}
	public void setSsn(String ssn) {
		this.ssn = ssn;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getZip() {
		return zip;
	}
	public void setZip(String zip) {
		this.zip = zip;
	}
	public PatientName getPatientName() {
		return patientName;
	}
	public void setPatientName(PatientName patientName) {
		this.patientName = patientName;
	}
	
}
