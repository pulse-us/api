package gov.ca.emsa.pulse.broker.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import gov.ca.emsa.pulse.broker.entity.GivenNameEntity;
import gov.ca.emsa.pulse.broker.entity.PatientRecordAddressEntity;
import gov.ca.emsa.pulse.broker.entity.PatientRecordAddressLineEntity;
import gov.ca.emsa.pulse.broker.entity.PatientRecordEntity;
import gov.ca.emsa.pulse.broker.entity.PatientRecordNameEntity;

public class PatientRecordDTO {
	private Long id;
	private String locationPatientRecordId;
	private String homeCommunityId;
	private List<PatientRecordNameDTO> patientRecordName;
	private PatientGenderDTO patientGender;
	private String dateOfBirth;
	private String ssn;
	private String phoneNumber;
	private List<PatientRecordAddressDTO> address;
	private Long queryLocationId;
	private Date lastModifiedDate;
	private Long patientGenderId;
	
	public PatientRecordDTO() {
		patientRecordName = new ArrayList<PatientRecordNameDTO>();
		address = new ArrayList<PatientRecordAddressDTO>();
	}
	
	public PatientRecordDTO(PatientRecordEntity entity) {
		this();
		this.id = entity.getId();
		if(entity.getPatientRecordName() != null && entity.getPatientRecordName().size() > 0) {
			for(PatientRecordNameEntity patientRecordNameEntity : entity.getPatientRecordName()){
				PatientRecordNameDTO patientRecordNameDTO = new PatientRecordNameDTO();
				patientRecordNameDTO.setFamilyName(patientRecordNameEntity.getFamilyName());
				ArrayList<GivenNameDTO> givens = new ArrayList<GivenNameDTO>();
				for(GivenNameEntity given : patientRecordNameEntity.getGivenNames()){
					GivenNameDTO givenDTO = new GivenNameDTO(given);
					givens.add(givenDTO);
				}
				patientRecordNameDTO.setGivenName(givens);
				patientRecordNameDTO.setSuffix(patientRecordNameEntity.getSuffix());
				patientRecordNameDTO.setPrefix(patientRecordNameEntity.getPrefix());
				if(patientRecordNameEntity.getNameType() != null){
					NameTypeDTO nameType = new NameTypeDTO(patientRecordNameEntity.getNameType());
					patientRecordNameDTO.setNameType(nameType);
				}
				if(patientRecordNameEntity.getNameRepresentation() != null){
					NameRepresentationDTO nameRep = new NameRepresentationDTO(patientRecordNameEntity.getNameRepresentation());
					patientRecordNameDTO.setNameRepresentation(nameRep);
				}
				if(patientRecordNameEntity.getNameAssembly() != null){
					NameAssemblyDTO nameAssembly = new NameAssemblyDTO(patientRecordNameEntity.getNameAssembly());
					patientRecordNameDTO.setNameAssembly(nameAssembly);
				}
				patientRecordNameDTO.setEffectiveDate(patientRecordNameEntity.getEffectiveDate());
				patientRecordNameDTO.setExpirationDate(patientRecordNameEntity.getExpirationDate());
				this.patientRecordName.add(patientRecordNameDTO);
			}
		}
		
		this.dateOfBirth = entity.getDateOfBirth();
		this.ssn = entity.getSsn();
		PatientGenderDTO patientGenderDTO = new PatientGenderDTO(entity.getPatientGender());
		this.patientGender = patientGenderDTO;
		this.patientGenderId = entity.getPatientGenderId();
		this.phoneNumber = entity.getPhoneNumber();
		this.locationPatientRecordId = entity.getLocationPatientRecordId();
		this.homeCommunityId = entity.getHomeCommunityId();
		
		List<PatientRecordAddressDTO> praArr = new ArrayList<PatientRecordAddressDTO>();
		for(PatientRecordAddressEntity pra : entity.getPatientRecordAddress()){
			PatientRecordAddressDTO praDto = new PatientRecordAddressDTO();
			ArrayList<PatientRecordAddressLineDTO> pralArr = new ArrayList<PatientRecordAddressLineDTO>();
			for(PatientRecordAddressLineEntity pralEntity : pra.getPatientRecordAddressLines()){
				PatientRecordAddressLineDTO pralDto = new PatientRecordAddressLineDTO(pralEntity);
				pralDto.setLine(pralEntity.getLine());
				pralDto.setLineOrder(pralEntity.getLineOrder());
				pralDto.setPatientRecordAddressId(pralEntity.getPatientRecordAddressId());
				pralArr.add(pralDto);
			}
			praDto.setPatientRecordAddressLines(pralArr);
			praDto.setCity(pra.getCity());
			praDto.setState(pra.getState());
			praDto.setZipcode(pra.getZipcode());
			praArr.add(praDto);
		}
		this.address = praArr;
		
		this.queryLocationId = entity.getQueryLocationId();
		this.lastModifiedDate = entity.getLastModifiedDate();
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
	public String getSsn() {
		return ssn;
	}
	public void setSsn(String ssn) {
		this.ssn = ssn;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public List<PatientRecordAddressDTO> getAddress() {
		return address;
	}
	public void setAddress(List<PatientRecordAddressDTO> address) {
		this.address = address;
	}

	public String getLocationPatientRecordId() {
		return locationPatientRecordId;
	}

	public void setLocationPatientRecordId(String locationPatientRecordId) {
		this.locationPatientRecordId = locationPatientRecordId;
	}

	public String getHomeCommunityId() {
		return homeCommunityId;
	}

	public void setHomeCommunityId(String homeCommunityId) {
		this.homeCommunityId = homeCommunityId;
	}

	public Long getQueryLocationId() {
		return queryLocationId;
	}

	public void setQueryLocationId(Long queryLocationId) {
		this.queryLocationId = queryLocationId;
	}

	public List<PatientRecordNameDTO> getPatientRecordName() {
		return patientRecordName;
	}

	public void setPatientRecordName(List<PatientRecordNameDTO> patientRecordName) {
		this.patientRecordName = patientRecordName;
	}

	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public PatientGenderDTO getPatientGender() {
		return patientGender;
	}

	public void setPatientGender(PatientGenderDTO patientGender) {
		this.patientGender = patientGender;
	}

	public Long getPatientGenderId() {
		return patientGenderId;
	}

	public void setPatientGenderId(Long patientGenderId) {
		this.patientGenderId = patientGenderId;
	}
	
}
