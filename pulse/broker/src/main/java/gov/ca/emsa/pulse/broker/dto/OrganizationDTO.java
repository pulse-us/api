package gov.ca.emsa.pulse.broker.dto;

import gov.ca.emsa.pulse.broker.entity.OrganizationEntity;

import java.util.Date;

public class OrganizationDTO {
	
	private Long id;
	private String name;
	private boolean isActive;
	private String adapter;
	private String ipAddress;
	private String username;
	private String password;
	private String certificationKey;
	private String endpointUrl;
	private Date creationDate;
	private Date lastModifiedDate;
	
	public OrganizationDTO(){}
	
	public OrganizationDTO(OrganizationEntity org){
		this.id = org.getId();
		this.name = org.getName();
		this.isActive = org.isActive();
		this.adapter = org.getAdapter();
		this.ipAddress = org.getIpAddress();
		this.username = org.getUsername();
		this.password = org.getPassword();
		this.certificationKey = org.getCertificationKey();
		this.endpointUrl = org.getEndpointUrl();
		this.creationDate = org.getCreationDate();
		this.lastModifiedDate = org.getLastModifiedDate();
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public String getAdapter() {
		return adapter;
	}

	public void setAdapter(String adapter) {
		this.adapter = adapter;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getCertificationKey() {
		return certificationKey;
	}

	public void setCertificationKey(String certificationKey) {
		this.certificationKey = certificationKey;
	}

	public String getEndpointUrl() {
		return endpointUrl;
	}

	public void setEndpointUrl(String endpointUrl) {
		this.endpointUrl = endpointUrl;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
	
	

}
