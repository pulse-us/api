package gov.ca.emsa.pulse.common.domain;


public class PatientRecord {
	private Long id;
	private PatientName patientName;
	private String dateOfBirth;
	private String gender;
	private String phoneNumber;
	private Address address;
	private String ssn;
	
	public PatientRecord() {
		patientName = new PatientName();
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
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
	}
	public String getSsn() {
		return ssn;
	}
	public void setSsn(String ssn) {
		this.ssn = ssn;
	}

	public PatientName getPatientName() {
		return patientName;
	}

	public void setPatientName(PatientName patientName) {
		this.patientName = patientName;
	}
	
	
	
}
