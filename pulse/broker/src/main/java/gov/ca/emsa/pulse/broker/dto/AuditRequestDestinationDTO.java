package gov.ca.emsa.pulse.broker.dto;

import java.util.Date;

import gov.ca.emsa.pulse.broker.entity.AuditRequestDestinationEntity;

public class AuditRequestDestinationDTO {
	
	private Long id;
	
	private String userId;
	
	private String alternativeUserId;
	
	private String userName;
	
	private boolean userIsRequestor;
	
	private String roleIdCode;
	
	private Long networkAccessPointTypeCodeId;
	
	private NetworkAccessPointTypeCodeDTO networkAccessPointTypeCode;
	
	private String networkAccessPointId;
	
	private Date creationDate;
	
	private Date lastModifiedDate;
	
	public AuditRequestDestinationDTO(){
		
	}
	
	public AuditRequestDestinationDTO(AuditRequestDestinationEntity entity){
		this.id = entity.getId();
		this.userId = entity.getUserId();
		this.alternativeUserId = entity.getAlternativeUserId();
		this.userName = entity.getUserName();
		this.userIsRequestor = entity.isUserIsRequestor();
		this.roleIdCode = entity.getRoleIdCode();
		this.networkAccessPointTypeCodeId = entity.getNetworkAccessPointTypeCodeId();
		//this.networkAccessPointTypeCode = new NetworkAccessPointTypeCodeDTO(entity.getNetworkAccessPointTypeCode());
		this.networkAccessPointId = entity.getNetworkAccessPointId();
		this.creationDate = entity.getCreationDate();
		this.lastModifiedDate = entity.getLastModifiedDate();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getAlternativeUserId() {
		return alternativeUserId;
	}

	public void setAlternativeUserId(String alternativeUserId) {
		this.alternativeUserId = alternativeUserId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public boolean isUserIsRequestor() {
		return userIsRequestor;
	}

	public void setUserIsRequestor(boolean userIsRequestor) {
		this.userIsRequestor = userIsRequestor;
	}

	public String getRoleIdCode() {
		return roleIdCode;
	}

	public void setRoleIdCode(String roleIdCode) {
		this.roleIdCode = roleIdCode;
	}

	public NetworkAccessPointTypeCodeDTO getNetworkAccessPointTypeCode() {
		return networkAccessPointTypeCode;
	}

	public void setNetworkAccessPointTypeCode(
			NetworkAccessPointTypeCodeDTO networkAccessPointTypeCode) {
		this.networkAccessPointTypeCode = networkAccessPointTypeCode;
	}

	public String getNetworkAccessPointId() {
		return networkAccessPointId;
	}

	public void setNetworkAccessPointId(String networkAccessPointId) {
		this.networkAccessPointId = networkAccessPointId;
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

	public Long getNetworkAccessPointTypeCodeId() {
		return networkAccessPointTypeCodeId;
	}

	public void setNetworkAccessPointTypeCodeId(Long networkAccessPointTypeCodeId) {
		this.networkAccessPointTypeCodeId = networkAccessPointTypeCodeId;
	}
	
	

}
