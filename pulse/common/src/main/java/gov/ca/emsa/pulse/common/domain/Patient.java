package gov.ca.emsa.pulse.common.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Patient {
	private Long id;
	private String orgPatientId;
	private PatientName patientName;
	private String dateOfBirth;
	private String gender;
	private String phoneNumber;
	private Address address;
	private String ssn;
	private Date lastRead;
	private AlternateCareFacility acf;
	private List<PatientOrganizationMap> orgMaps;
	
	public Patient() {
		this.orgMaps = new ArrayList<PatientOrganizationMap>();
		this.patientName = new PatientName();
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getOrgPatientId() {
		return orgPatientId;
	}

	public void setOrgPatientId(String orgPatientId) {
		this.orgPatientId = orgPatientId;
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
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
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

	public List<PatientOrganizationMap> getOrgMaps() {
		return orgMaps;
	}

	public void setOrgMaps(List<PatientOrganizationMap> orgMaps) {
		this.orgMaps = orgMaps;
	}

	public Date getLastRead() {
		return lastRead;
	}

	public void setLastRead(Date lastRead) {
		this.lastRead = lastRead;
	}

	public PatientName getPatientName() {
		return patientName;
	}

	public void setPatientName(PatientName patientName) {
		this.patientName = patientName;
	}

}
