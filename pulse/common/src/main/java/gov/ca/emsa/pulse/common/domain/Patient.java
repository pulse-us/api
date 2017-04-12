package gov.ca.emsa.pulse.common.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Patient {
	private Long id;
	private String externalPatientId;
	private String fullName;
	private String friendlyName;
	private String dateOfBirth;
	private String gender;
	private String phoneNumber;
	private String ssn;
	private Date lastRead;
	private AlternateCareFacility acf;
	private List<PatientEndpointMap> endpointMaps;
	private Date creationDate;
	
	public Patient() {
		this.endpointMaps = new ArrayList<PatientEndpointMap>();
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getExternalPatientId() {
		return externalPatientId;
	}

	public void setExternalPatientId(String externalPatientId) {
		this.externalPatientId = externalPatientId;
	}

	public String getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
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
	public AlternateCareFacility getAcf() {
		return acf;
	}
	public void setAcf(AlternateCareFacility acf) {
		this.acf = acf;
	}
	public String getSsn() {
		return ssn;
	}
	public void setSsn(String ssn) {
		this.ssn = ssn;
	}

	public List<PatientEndpointMap> getEndpointMaps() {
		return endpointMaps;
	}

	public void setEndpointMaps(List<PatientEndpointMap> endpointMaps) {
		this.endpointMaps = endpointMaps;
	}

	public Date getLastRead() {
		return lastRead;
	}

	public void setLastRead(Date lastRead) {
		this.lastRead = lastRead;
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
