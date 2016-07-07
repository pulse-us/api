package gov.ca.emsa.pulse.broker.domain;

import java.util.Date;

import gov.ca.emsa.pulse.broker.dto.PatientRecordDTO;

public class PatientRecord {
	private Long id;
	private String firstName;
	private String lastName;
	private Date dateOfBirth;
	private String gender;
	private String phoneNumber;
	private Address address;
	private String ssn;
	
	public PatientRecord() {
		
	}
	
	public PatientRecord(PatientRecordDTO dto) {
		this.id = dto.getId();
		this.firstName = dto.getFirstName();
		this.lastName = dto.getLastName();
		this.dateOfBirth = dto.getDateOfBirth();
		this.gender = dto.getGender();
		this.phoneNumber = dto.getPhoneNumber();
		if(dto.getAddress() != null) {
			this.address = new Address(dto.getAddress());
		}
		this.ssn = dto.getSsn();
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public Date getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(Date dateOfBirth) {
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
	
}
