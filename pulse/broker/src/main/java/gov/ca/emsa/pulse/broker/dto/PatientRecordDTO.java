package gov.ca.emsa.pulse.broker.dto;

import gov.ca.emsa.pulse.broker.entity.GivenNameEntity;
import gov.ca.emsa.pulse.broker.entity.PatientRecordNameEntity;
import gov.ca.emsa.pulse.broker.entity.PatientRecordEntity;

import java.time.LocalDate;
import java.util.ArrayList;

public class PatientRecordDTO {
	private Long id;
	private String orgPatientId;
	private ArrayList<PatientRecordNameDTO> PatientRecordName;
	private LocalDate dateOfBirth;
	private String ssn;
	private String gender;
	private String phoneNumber;
	private AddressDTO address;
	private Long queryOrganizationId;
	
	public PatientRecordDTO() {
		PatientRecordName = new ArrayList<PatientRecordNameDTO>();
	}
	
	public PatientRecordDTO(PatientRecordEntity entity) {
		this.id = entity.getId();
		this.orgPatientId = entity.getOrgPatientId();
		for(PatientRecordNameEntity PatientRecordNameEntity : entity.getPatientRecordName()){
			PatientRecordNameDTO PatientRecordNameDTO = new PatientRecordNameDTO();
			PatientRecordNameDTO.setFamilyName(PatientRecordNameEntity.getFamilyName());
			ArrayList<GivenNameDTO> givens = new ArrayList<GivenNameDTO>();
			for(GivenNameEntity given : PatientRecordNameEntity.getGivenNames()){
				GivenNameDTO givenDTO = new GivenNameDTO(given);
				givens.add(givenDTO);
			}
			PatientRecordNameDTO.setGivenName(givens);
			PatientRecordNameDTO.setSuffix(PatientRecordNameEntity.getSuffix());
			PatientRecordNameDTO.setPrefix(PatientRecordNameEntity.getPrefix());
			if(PatientRecordNameEntity.getNameType() != null){
				NameTypeDTO nameType = new NameTypeDTO(PatientRecordNameEntity.getNameType());
				PatientRecordNameDTO.setNameType(nameType);
			}
			if(PatientRecordNameEntity.getNameRepresentation() != null){
				NameRepresentationDTO nameRep = new NameRepresentationDTO(PatientRecordNameEntity.getNameRepresentation());
				PatientRecordNameDTO.setNameRepresentation(nameRep);
			}
			if(PatientRecordNameEntity.getNameAssembly() != null){
				NameAssemblyDTO nameAssembly = new NameAssemblyDTO(PatientRecordNameEntity.getNameAssembly());
				PatientRecordNameDTO.setNameAssembly(nameAssembly);
			}
			PatientRecordNameDTO.setEffectiveDate(PatientRecordNameEntity.getEffectiveDate());
			PatientRecordNameDTO.setExpirationDate(PatientRecordNameEntity.getExpirationDate());
		}
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

	public ArrayList<PatientRecordNameDTO> getPatientRecordName() {
		return PatientRecordName;
	}

	public void setPatientRecordName(ArrayList<PatientRecordNameDTO> PatientRecordName) {
		this.PatientRecordName = PatientRecordName;
	}
	
}
