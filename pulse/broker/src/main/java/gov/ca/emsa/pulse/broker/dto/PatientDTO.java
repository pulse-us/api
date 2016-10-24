package gov.ca.emsa.pulse.broker.dto;

import gov.ca.emsa.pulse.broker.entity.PatientEntity;
import gov.ca.emsa.pulse.broker.entity.PatientRecordNameEntity;
import gov.ca.emsa.pulse.broker.entity.PatientOrganizationMapEntity;
import gov.ca.emsa.pulse.common.domain.Address;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PatientDTO {
	private Long id;
	private String fullName;
	private String friendlyName;
	private String dateOfBirth;
	private String ssn;
	private String gender;
	private AddressDTO address;
	private Date lastReadDate;
	private AlternateCareFacilityDTO acf;
	private List<PatientOrganizationMapDTO> orgMaps;
	
	public PatientDTO() {
		orgMaps = new ArrayList<PatientOrganizationMapDTO>();
	}
	
	public PatientDTO(PatientEntity entity) {
		this();
		this.id = entity.getId();
		this.fullName = entity.getFullName();
		this.friendlyName = entity.getFriendlyName();
		if(entity.getDateOfBirth() != null) {
			this.dateOfBirth = entity.getDateOfBirth();
		}
		this.ssn = entity.getSsn();
		this.gender = entity.getGender();
		this.address = new AddressDTO(entity.getAddress());
		this.lastReadDate = entity.getLastReadDate();
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
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
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

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getFriendlyName() {
		return friendlyName;
	}

	public void setFriendlyName(String friendlyName) {
		this.friendlyName = friendlyName;
	}

	public AddressDTO getAddress() {
		return address;
	}

	public void setAddress(AddressDTO address) {
		this.address = address;
	}
	
}
