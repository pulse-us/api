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
	
	@Column(name="user_id")
	private Long userId;
	
	@Column(name="alternative_user_id")
	private Long alternativeUserId;
	
	@Column(name="user_name")
	private String userName;
	
	@Column(name="user_is_requestor")
	private boolean userIsRequestor;
	
	@Column(name="role_id_code")
	private String roleIdCode;
	
	@Column(name="network_access_point_type_code") // "1" for machine (DNS) name, "2" for IP address
	private String networkAccessPointTypeCode;
	
	@Column(name="network_access_point_id") // the machine name or IP address
	private Long networkAccessPointId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getAlternativeUserId() {
		return alternativeUserId;
	}

	public void setAlternativeUserId(Long alternativeUserId) {
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

	public Long getNetworkAccessPointId() {
		return networkAccessPointId;
	}

	public void setNetworkAccessPointId(Long networkAccessPointId) {
		this.networkAccessPointId = networkAccessPointId;
	}
	
}
