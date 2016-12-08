package gov.ca.emsa.pulse.broker.dto;

import gov.ca.emsa.pulse.broker.entity.AuditHumanRequestorEntity;

public class AuditHumanRequestorDTO {
	
	private Long id;
	
	private String userId;
	
	private Long auditEventId;
	
	private String alternativeUserId;
	
	private String userName;
	
	private boolean userIsRequestor;
	
	private String roleIdCode;
	
	private String networkAccessPointTypeCode;
	
	private String networkAccessPointId;
	
	public AuditHumanRequestorDTO(){
		
	}
	
	public AuditHumanRequestorDTO(AuditHumanRequestorEntity entity){
		this.userId = entity.getUserId();
		this.alternativeUserId = entity.getAlternativeUserId();
		this.auditEventId = entity.getAuditEventId();
		this.userName = entity.getUserName();
		this.userIsRequestor = entity.isUserIsRequestor();
		this.roleIdCode = entity.getRoleIdCode();
		this.networkAccessPointTypeCode = entity.getNetworkAccessPointTypeCode();
		this.networkAccessPointId = entity.getNetworkAccessPointId();
		
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

	public void setUserId(String string) {
		this.userId = string;
	}

	public void setAlternativeUserId(String alternativeUserId) {
		this.alternativeUserId = alternativeUserId;
	}

	public String getAlternativeUserId() {
		return alternativeUserId;
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

	public String getNetworkAccessPointTypeCode() {
		return networkAccessPointTypeCode;
	}

	public void setNetworkAccessPointTypeCode(String networkAccessPointTypeCode) {
		this.networkAccessPointTypeCode = networkAccessPointTypeCode;
	}

	public String getNetworkAccessPointId() {
		return networkAccessPointId;
	}

	public void setNetworkAccessPointId(String string) {
		this.networkAccessPointId = string;
	}

	public Long getAuditEventId() {
		return auditEventId;
	}

	public void setAuditEventId(Long auditEventId) {
		this.auditEventId = auditEventId;
	}
	
}
