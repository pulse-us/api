package gov.ca.emsa.pulse.broker.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import gov.ca.emsa.pulse.broker.dto.AddressDTO;
import gov.ca.emsa.pulse.broker.dto.AlternateCareFacilityDTO;
import gov.ca.emsa.pulse.broker.dto.PatientDTO;
import gov.ca.emsa.pulse.broker.dto.PatientOrganizationMapDTO;

public class Patient {
	private Long id;
	private String firstName;
	private String lastName;
	private Date dateOfBirth;
	private String gender;
	private String phoneNumber;
	private Address address;
	private String ssn;
	private AlternateCareFacility acf;
	private List<PatientOrganizationMap> orgMaps;
	
	public Patient() {
		this.orgMaps = new ArrayList<PatientOrganizationMap>();
	}
	
	public Patient(PatientDTO dto) {
		this();
		this.id = dto.getId();
		this.firstName = dto.getFirstName();
		this.lastName = dto.getLastName();
		this.dateOfBirth = dto.getDateOfBirth();
		this.ssn = dto.getSsn();
		this.gender = dto.getGender();
		this.phoneNumber = dto.getPhoneNumber();
		this.address = new Address(dto.getAddress());
		if(dto.getAcf() != null) {
			this.acf = new AlternateCareFacility(dto.getAcf());
		}
		if(dto.getOrgMaps() != null && dto.getOrgMaps().size() > 0) {
			for(PatientOrganizationMapDTO orgMapDto : dto.getOrgMaps()) {
				PatientOrganizationMap orgMap = new PatientOrganizationMap(orgMapDto);
				this.orgMaps.add(orgMap);
			}
		}
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public Date getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(Date dateOfBirth) {
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
	
}
