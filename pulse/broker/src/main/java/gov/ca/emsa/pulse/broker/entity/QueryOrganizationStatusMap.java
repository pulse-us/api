package gov.ca.emsa.pulse.broker.entity;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="query_organization", schema="pulse")
public class QueryOrganizationStatusMap {
	
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column( name = "id", nullable = false )
	private Long id;
	
	@Column(name="query_id")
	private Long queryId;
	
	@Basic( optional = true )
	@OneToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "query_id", unique=true, nullable = true, insertable=false, updatable=false)
	private QueryEntity query;
	
	@Column(name = "organization_id")
	private Long organizationId;
	
	@Basic( optional = true )
	@OneToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "organization_id", unique=true, nullable = true, insertable=false, updatable=false)
	private OrganizationEntity org;
	
	@Column(name="status")
	private String status;
	
	@Column(name = "start_date", insertable = false, updatable = false)
	private Date startDate;
	
	@Column(name = "end_date")
	private Date endDate;
	
	@Column(name = "from_cache")
	private Boolean fromCache;
	
	@Column(name = "success")
	private Boolean success;
	
	@Column( name = "creation_date", insertable = false, updatable = false)
	private Date creationDate;
	
	@Column( name = "last_modified_date", insertable = false, updatable = false)
	private Date lastModifiedDate;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long long1) {
		this.id = long1;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getQueryId() {
		return queryId;
	}

	public void setQueryId(Long queryId) {
		this.queryId = queryId;
	}

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public QueryEntity getQuery() {
		return query;
	}

	public void setQuery(QueryEntity query) {
		this.query = query;
	}

	public OrganizationEntity getOrg() {
		return org;
	}

	public void setOrg(OrganizationEntity org) {
		this.org = org;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Boolean getFromCache() {
		return fromCache;
	}

	public void setFromCache(Boolean fromCache) {
		this.fromCache = fromCache;
	}

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}
}