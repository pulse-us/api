package gov.ca.emsa.pulse.broker.entity;

import java.util.Date;

import gov.ca.emsa.pulse.broker.dto.NetworkAccessPointTypeCodeDTO;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="audit_request_source")
public class AuditRequestSourceEntity {
	
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column( name = "id", nullable = false)
	private Long id;
	
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
	
	@Column(name="network_access_point_type_code_id")
	private Long networkAccessPointTypeCodeId;
	
	@OneToOne
	@JoinColumn(name="network_access_point_type_code_id", insertable = false, updatable = false)
	private NetworkAccessPointTypeCodeEntity networkAccessPointTypeCode;
	
	@Column(name="network_access_point_id")
	private String networkAccessPointId;
	
	@Column( name = "creation_date", insertable = false, updatable = false)
	private Date creationDate;
	
	@Column( name = "last_modified_date", insertable = false, updatable = false)
	private Date lastModifiedDate;

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

	public NetworkAccessPointTypeCodeEntity getNetworkAccessPointTypeCode() {
		return networkAccessPointTypeCode;
	}

	public void setNetworkAccessPointTypeCode(
			NetworkAccessPointTypeCodeEntity networkAccessPointTypeCode) {
		this.networkAccessPointTypeCode = networkAccessPointTypeCode;
	}

	public String getNetworkAccessPointId() {
		return networkAccessPointId;
	}

	public void setNetworkAccessPointId(String networkAccessPointId) {
		this.networkAccessPointId = networkAccessPointId;
	}

	public Long getNetworkAccessPointTypeCodeId() {
		return networkAccessPointTypeCodeId;
	}

	public void setNetworkAccessPointTypeCodeId(Long networkAccessPointTypeCodeId) {
		this.networkAccessPointTypeCodeId = networkAccessPointTypeCodeId;
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
