package gov.ca.emsa.pulse.broker.dto;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;

import gov.ca.emsa.pulse.broker.entity.GivenNameEntity;
import gov.ca.emsa.pulse.broker.entity.PatientRecordEntity;
import gov.ca.emsa.pulse.common.domain.PatientName;

public class PatientRecordDTO {
	private Long id;
	private String orgPatientId;
	private PatientNameDTO patientName;
	private LocalDate dateOfBirth;
	private String ssn;
	private String gender;
	private String phoneNumber;
	private AddressDTO address;
	private Long queryOrganizationId;
	
	public PatientRecordDTO() {
		patientName = new PatientNameDTO();
	}
	
	public PatientRecordDTO(PatientRecordEntity entity) {
		this.id = entity.getId();
		this.orgPatientId = entity.getOrgPatientId();
		this.patientName = new PatientNameDTO();
		this.patientName.setFamilyName(entity.getPatientName().getFamilyName());
		ArrayList<GivenNameDTO> givens = new ArrayList<GivenNameDTO>();
		for(GivenNameEntity given : entity.getPatientName().getGivenNames()){
			GivenNameDTO givenDTO = new GivenNameDTO(given);
			givens.add(givenDTO);
		}
		this.patientName.setGivenName(givens);
		this.patientName.setSuffix(entity.getPatientName().getSuffix());
		this.patientName.setPrefix(entity.getPatientName().getPrefix());
		this.patientName.setNameTypeCode(entity.getPatientName().getNameTypeCode());
		this.patientName.setNameTypeCodeDescription(entity.getPatientName().getNameTypeCodeDescription());
		this.patientName.setNameRepresentationCode(entity.getPatientName().getNameRepresentationCode());
		this.patientName.setNameRepresentationCodeDescription(entity.getPatientName().getNameRepresentationCodeDescription());
		this.patientName.setNameAssemblyOrderCode(entity.getPatientName().getNameAssemblyOrderCode());
		this.patientName.setNameAssemblyOrderCodeDescription(entity.getPatientName().getNameAssemblyOrderCodeDescription());
		this.patientName.setEffectiveDate(entity.getPatientName().getEffectiveDate());
		this.patientName.setExpirationDate(entity.getPatientName().getExpirationDate());
		
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

	public PatientNameDTO getPatientName() {
		return patientName;
	}

	public void setPatientName(PatientNameDTO patientName) {
		this.patientName = patientName;
	}
	
}
