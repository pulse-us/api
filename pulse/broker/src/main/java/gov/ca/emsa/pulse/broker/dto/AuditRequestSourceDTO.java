package gov.ca.emsa.pulse.broker.dto;

import gov.ca.emsa.pulse.broker.entity.AuditRequestSourceEntity;

public class AuditRequestSourceDTO {
	
	private Long id;
	
	private String userId;
	
	private String alternativeUserId;
	
	private String userName;
	
	private boolean userIsRequestor;
	
	private String roleIdCode;
	
	private String networkAccessPointTypeCode;
	
	private String networkAccessPointId;
	
	public AuditRequestSourceDTO(){
		
	}
	
	public AuditRequestSourceDTO(AuditRequestSourceEntity entity){
		this.id = entity.getId();
		this.userId = entity.getUserId();
		this.alternativeUserId = entity.getAlternativeUserId();
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

	public String getNetworkAccessPointTypeCode() {
		return networkAccessPointTypeCode;
	}

	public void setNetworkAccessPointTypeCode(String networkAccessPointTypeCode) {
		this.networkAccessPointTypeCode = networkAccessPointTypeCode;
	}

	public String getNetworkAccessPointId() {
		return networkAccessPointId;
	}

	public void setNetworkAccessPointId(String networkAccessPointId) {
		this.networkAccessPointId = networkAccessPointId;
	}

}
