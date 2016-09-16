package gov.ca.emsa.pulse.broker.dto;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Date;

import gov.ca.emsa.pulse.broker.entity.PatientRecordEntity;

public class PatientRecordDTO {
	private Long id;
	private String orgPatientId;
	private String givenName;
	private String familyName;
	private LocalDate dateOfBirth;
	private String ssn;
	private String gender;
	private String phoneNumber;
	private AddressDTO address;
	private Long queryOrganizationId;
	
	public PatientRecordDTO() {
	}
	
	public PatientRecordDTO(PatientRecordEntity entity) {
		this.id = entity.getId();
		this.orgPatientId = entity.getOrgPatientId();
		this.givenName = entity.getGivenName();
		this.familyName = entity.getFamilyName();
		if(entity.getDateOfBirth() != null) {
			this.dateOfBirth = entity.getDateOfBirth().toLocalDate();
		}
		this.ssn = entity.getSsn();
		this.gender = entity.getGender();
		this.phoneNumber = entity.getPhoneNumber();
		
		this.address = new AddressDTO();
		this.address.setStreetLineOne(entity.getStreetLineOne());
		this.address.setStreetLineTwo(entity.getStreetLineTwo());
		this.address.setCity(entity.getCity());
		this.address.setState(entity.getState());
		this.address.setZipcode(entity.getZipcode());
		this.address.setCountry(entity.getCountry());
		
		this.queryOrganizationId = entity.getQueryOrganizationId();
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(LocalDate dateOfBirth) {
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

	public String getOrgPatientId() {
		return orgPatientId;
	}

	public void setOrgPatientId(String orgPatientId) {
		this.orgPatientId = orgPatientId;
	}

	public Long getQueryOrganizationId() {
		return queryOrganizationId;
	}

	public void setQueryOrganizationId(Long queryOrganizationId) {
		this.queryOrganizationId = queryOrganizationId;
	}

	public String getGivenName() {
		return givenName;
	}

	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}

	public String getFamilyName() {
		return familyName;
	}

	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}
}
