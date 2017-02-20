package gov.ca.emsa.pulse.common.domain;

import java.util.Date;

public class AlternateCareFacility {
	private Long id;
	private String name;
	private String friendlyName;
	private String phoneNumber;
	private Address address;
	private Date lastRead;
	
	public AlternateCareFacility() {}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address acfAddr) {
		this.address = acfAddr;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public void setLastRead(Date lastRead) {
		this.lastRead = lastRead;
	}

	public Date getLastRead() {
		return lastRead;
	}

	public String getFriendlyName() {
		return friendlyName;
	}

	public void setFriendlyName(String friendlyName) {
		this.friendlyName = friendlyName;
	}
}
