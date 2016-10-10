package gov.ca.emsa.pulse.broker.dto;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import gov.ca.emsa.pulse.broker.entity.PatientEntity;
import gov.ca.emsa.pulse.broker.entity.PatientOrganizationMapEntity;
import gov.ca.emsa.pulse.common.domain.PatientName;

public class PatientDTO {
	private Long id;
	private PatientNameDTO patientName;
	private LocalDate dateOfBirth;
	private String ssn;
	private String gender;
	private String phoneNumber;
	private Date lastReadDate;
	private AddressDTO address;
	private AlternateCareFacilityDTO acf;
	private List<PatientOrganizationMapDTO> orgMaps;
	
	public PatientDTO() {
		orgMaps = new ArrayList<PatientOrganizationMapDTO>();
		patientName = new PatientNameDTO();
	}
	
	public PatientDTO(PatientEntity entity) {
		this();
		this.id = entity.getId();
		if(entity.getPatientName() != null){
			this.patientName = new PatientNameDTO(entity.getPatientName());
		}
		if(entity.getDateOfBirth() != null) {
			this.dateOfBirth = entity.getDateOfBirth().toLocalDate();
		}
		this.ssn = entity.getSsn();
		this.gender = entity.getGender();
		this.phoneNumber = entity.getPhoneNumber();
		this.lastReadDate = entity.getLastReadDate();
		this.address = new AddressDTO(entity.getAddress());
		if(entity.getAcf() != null) {
			this.acf = new AlternateCareFacilityDTO(entity.getAcf());
		}
		if(entity.getOrgMaps() != null && entity.getOrgMaps().size() > 0) {
			for(PatientOrganizationMapEntity orgMap : entity.getOrgMaps()) {
				PatientOrganizationMapDTO orgMapDto = new PatientOrganizationMapDTO(orgMap);
				this.orgMaps.add(orgMapDto);
			}
		}
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

	public AlternateCareFacilityDTO getAcf() {
		return acf;
	}

	public void setAcf(AlternateCareFacilityDTO acf) {
		this.acf = acf;
	}

	public Date getLastReadDate() {
		return lastReadDate;
	}

	public void setLastReadDate(Date lastReadDate) {
		this.lastReadDate = lastReadDate;
	}

	public List<PatientOrganizationMapDTO> getOrgMaps() {
		return orgMaps;
	}

	public void setOrgMaps(List<PatientOrganizationMapDTO> orgMaps) {
		this.orgMaps = orgMaps;
	}

	public PatientNameDTO getPatientName() {
		return patientName;
	}

	public void setPatientName(PatientNameDTO patientName) {
		this.patientName = patientName;
	}
	
}
