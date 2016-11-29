package gov.ca.emsa.pulse.broker.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="audit_human_requestor")
public class AuditHumanRequestorEntity {
	
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column( name = "id", nullable = false )
	private Long id;
	
	@Column(name="audit_event_id")
	private Long auditEventId;
	
	@Column(name="user_id")
	private String userId;
	
	@Column(name="alternative_user_id")
	private String alternativeUserId;
	
	@Column(name="user_name")
	private String userName;
	
	@Column(name="user_is_requestor")
	private boolean userIsRequestor;
	
	@Column(name="role_id_code")
	private String roleIdCode;
	
	@Column(name="network_access_point_type_code")
	private String networkAccessPointTypeCode;
	
	@Column(name="network_access_point_id")
	private String networkAccessPointId;

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

	public Long getAuditEventId() {
		return auditEventId;
	}

	public void setAuditEventId(Long auditEventId) {
		this.auditEventId = auditEventId;
	}
	
	
	
}
