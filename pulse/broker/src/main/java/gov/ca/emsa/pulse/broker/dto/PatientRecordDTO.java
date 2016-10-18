package gov.ca.emsa.pulse.broker.dto;

import gov.ca.emsa.pulse.broker.entity.GivenNameEntity;
import gov.ca.emsa.pulse.broker.entity.PatientOrganizationMapEntity;
import gov.ca.emsa.pulse.broker.entity.PatientRecordNameEntity;
import gov.ca.emsa.pulse.broker.entity.PatientRecordEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PatientRecordDTO {
	private Long id;
	private String orgPatientId;
	private List<PatientRecordNameDTO> patientRecordName;
	private LocalDate dateOfBirth;
	private String ssn;
	private String gender;
	private String phoneNumber;
	private AddressDTO address;
	private Long queryOrganizationId;
	private List<PatientOrganizationMapDTO> orgMaps;
	
	public PatientRecordDTO() {
		patientRecordName = new ArrayList<PatientRecordNameDTO>();
	}
	
	public PatientRecordDTO(PatientRecordEntity entity) {
		this();
		this.id = entity.getId();
		this.orgPatientId = entity.getOrgPatientId();
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
		if(entity.getOrgMaps() != null && entity.getOrgMaps().size() > 0) {
			for(PatientOrganizationMapEntity orgMap : entity.getOrgMaps()) {
				PatientOrganizationMapDTO orgMapDto = new PatientOrganizationMapDTO(orgMap);
				this.orgMaps.add(orgMapDto);
			}
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

	public List<PatientRecordNameDTO> getPatientRecordName() {
		return patientRecordName;
	}

	public void setPatientRecordName(List<PatientRecordNameDTO> patientRecordName) {
		this.patientRecordName = patientRecordName;
	}

	public List<PatientOrganizationMapDTO> getOrgMaps() {
		return orgMaps;
	}

	public void setOrgMaps(List<PatientOrganizationMapDTO> orgMaps) {
		this.orgMaps = orgMaps;
	}
	
}
