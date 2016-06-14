package gov.ca.emsa.pulse.broker.dto;

import java.util.Date;

import gov.ca.emsa.pulse.broker.entity.PatientEntity;

public class PatientDTO {
	private Long id;
	private String pulsePatientId;
	private String orgPatientId;
	private String firstName;
	private String lastName;
	private Date dateOfBirth;
	private String ssn;
	private String gender;
	private String phoneNumber;
	private AddressDTO address;
	private OrganizationDTO organization;
	
	public PatientDTO() {
	}
	
	public PatientDTO(PatientEntity entity) {
		this.id = entity.getId();
		this.pulsePatientId = entity.getPulsePatientId();
		this.orgPatientId = entity.getOrgPatientId();
		this.firstName = entity.getFirstName();
		this.lastName = entity.getLastName();
		this.dateOfBirth = entity.getDateOfBirth();
		this.ssn = entity.getSsn();
		this.gender = entity.getGender();
		this.phoneNumber = entity.getPhoneNumber();
		if(entity.getAddress() != null) {
			this.address = new AddressDTO(entity.getAddress());
		}
		if(entity.getOrganization() != null) {
			this.organization = new OrganizationDTO(entity.getOrganization());
		}
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
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public AddressDTO getAddress() {
		return address;
	}
	public void setAddress(AddressDTO address) {
		this.address = address;
	}
	public OrganizationDTO getOrganization() {
		return organization;
	}
	public void setOrganization(OrganizationDTO organization) {
		this.organization = organization;
	}

	public String getPulsePatientId() {
		return pulsePatientId;
	}

	public void setPulsePatientId(String pulsePatientId) {
		this.pulsePatientId = pulsePatientId;
	}

	public String getOrgPatientId() {
		return orgPatientId;
	}

	public void setOrgPatientId(String orgPatientId) {
		this.orgPatientId = orgPatientId;
	}
}
