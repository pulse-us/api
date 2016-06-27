package gov.ca.emsa.pulse.broker.entity;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="organization")
public class OrganizationEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column( name = "id", nullable = false )
	private Long id;
	
	@Basic( optional = false )
	@Column( name = "organization_id", nullable = false )
	private Long organizationId;
	
	@Basic( optional = false )
	@Column( name = "name", nullable = false )
	private String name;
	
	@Basic( optional = false )
	@Column( name = "is_active", nullable = false )
	private boolean isActive;
	
	@Basic( optional = false )
	@Column( name = "adapter", nullable = false )
	private String adapter;
	
	@Basic( optional = false )
	@Column( name = "ip_address", nullable = false )
	private String ipAddress;
	
	@Column( name = "username", nullable = true )
	private String username;
	
	@Column( name = "password", nullable = true )
	private String password;
	
	@Column( name = "certification_key", nullable = true )
	private String certificationKey;
	
	@Column( name = "endpoint_url", nullable = true )
	private String endpointUrl;
	
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

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
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
