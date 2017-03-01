package gov.ca.emsa.pulse.broker.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import gov.ca.emsa.pulse.broker.entity.PatientEntity;
import gov.ca.emsa.pulse.broker.entity.PatientLocationMapEntity;

public class PatientDTO {
	private Long id;
	private String fullName;
	private String friendlyName;
	private String dateOfBirth;
	private String ssn;
	private String gender;
	private Date lastReadDate;
	private AlternateCareFacilityDTO acf;
	private List<PatientLocationMapDTO> locationMaps;
	private Date creationDate;
	
	public PatientDTO() {
		locationMaps = new ArrayList<PatientLocationMapDTO>();
	}
	
	public PatientDTO(PatientEntity entity) {
		this();
		this.id = entity.getId();
		this.fullName = entity.getFullName();
		this.friendlyName = entity.getFriendlyName();
		this.dateOfBirth = entity.getDateOfBirth();
		this.ssn = entity.getSsn();
		this.gender = entity.getGender();
		this.lastReadDate = entity.getLastReadDate();
		this.creationDate = entity.getCreationDate();
		if(entity.getAcf() != null) {
			this.acf = new AlternateCareFacilityDTO(entity.getAcf());
		}
		if(entity.getLocationMaps() != null && entity.getLocationMaps().size() > 0) {
			for(PatientLocationMapEntity locationMap : entity.getLocationMaps()) {
				PatientLocationMapDTO locationMapDto = new PatientLocationMapDTO(locationMap);
				this.locationMaps.add(locationMapDto);
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

	public List<PatientLocationMapDTO> getLocationMaps() {
		return locationMaps;
	}

	public void setLocationMaps(List<PatientLocationMapDTO> locationMaps) {
		this.locationMaps = locationMaps;
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

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	
}
