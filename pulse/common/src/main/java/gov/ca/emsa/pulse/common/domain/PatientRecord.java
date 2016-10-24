package gov.ca.emsa.pulse.common.domain;

import java.util.ArrayList;


public class PatientRecord {
	private Long id;
	private ArrayList<PatientRecordName> patientRecordName;
	private String organizationPatientRecordId;
	private String dateOfBirth;
	private String gender;
	private String phoneNumber;
	private PatientRecordAddress address;
	private String ssn;
	
	public PatientRecord() {
		patientRecordName = new ArrayList<PatientRecordName>();
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public String getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public PatientRecordAddress getAddress() {
		return address;
	}
	public void setAddress(PatientRecordAddress address) {
		this.address = address;
	}
	public String getSsn() {
		return ssn;
	}
	public void setSsn(String ssn) {
		this.ssn = ssn;
	}

	public ArrayList<PatientRecordName> getPatientRecordName() {
		return patientRecordName;
	}

	public void setPatientRecordName(ArrayList<PatientRecordName> patientRecordName) {
		this.patientRecordName = patientRecordName;
	}

	public String getOrganizationPatientRecordId() {
		return organizationPatientRecordId;
	}

	public void setOrganizationPatientRecordId(String organizationPatientRecordId) {
		this.organizationPatientRecordId = organizationPatientRecordId;
	}
	
}
