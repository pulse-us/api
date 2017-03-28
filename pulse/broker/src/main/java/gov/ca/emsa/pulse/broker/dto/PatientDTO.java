package gov.ca.emsa.pulse.broker.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import gov.ca.emsa.pulse.broker.entity.PatientEntity;
import gov.ca.emsa.pulse.broker.entity.PatientEndpointMapEntity;

public class PatientDTO {
	private Long id;
	private String fullName;
	private String friendlyName;
	private String dateOfBirth;
	private String ssn;
	private String gender;
	private Date lastReadDate;
	private AlternateCareFacilityDTO acf;
	private List<PatientEndpointMapDTO> endpointMaps;
	private Date creationDate;
	
	public PatientDTO() {
		endpointMaps = new ArrayList<PatientEndpointMapDTO>();
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
		if(entity.getEndpointMaps() != null && entity.getEndpointMaps().size() > 0) {
			for(PatientEndpointMapEntity endpointMap : entity.getEndpointMaps()) {
				PatientEndpointMapDTO endpointMapDto = new PatientEndpointMapDTO(endpointMap);
				this.endpointMaps.add(endpointMapDto);
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

	public List<PatientEndpointMapDTO> getEndpointMaps() {
		return endpointMaps;
	}

	public void setEndpointMaps(List<PatientEndpointMapDTO> endpointMaps) {
		this.endpointMaps = endpointMaps;
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
