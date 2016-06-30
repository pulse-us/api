package gov.ca.emsa.pulse.broker.domain;

import gov.ca.emsa.pulse.broker.dto.OrganizationDTO;

public class Organization extends OrganizationBase {
	
	private String ipAddress;
	private String username;
	private String password;
	private String certificationKey;
	private String endpointUrl;
	
	public Organization(){}
	
	public Organization(String name){
		this.setName(name);
	}
	
	public Organization(OrganizationDTO dto) {
		this.ipAddress = dto.getIpAddress();
		this.username = dto.getUsername();
		this.password = dto.getPassword();
		this.certificationKey = dto.getCertificationKey();
		this.endpointUrl = dto.getEndpointUrl();
	}
	
	public String getEndpointUrl() {
		return endpointUrl;
	}

	public void setEndpointUrl(String endpointUrl) {
		this.endpointUrl = endpointUrl;
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
}
