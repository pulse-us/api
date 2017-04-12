package gov.ca.emsa.pulse.broker.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="query_endpoint_map")
public class QueryEndpointMapEntity {
	
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column( name = "id", nullable = false )
	private Long id;
	
	@Column(name="query_id")
	private Long queryId;
	
	@Column(name = "endpoint_id")
	private Long endpointId;
	
	@Basic( optional = true )
	@OneToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "endpoint_id", unique=true, nullable = true, insertable=false, updatable=false)
	private EndpointEntity endpoint;
	
	@Column(name = "query_endpoint_status_id")
	private Long statusId;
	
	@Basic( optional = true )
	@OneToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "query_endpoint_status_id", unique=true, nullable = false, insertable=false, updatable= false)
	private QueryEndpointStatusEntity status;
	
	@Column(name = "start_date", insertable = false, updatable = false)
	private Date startDate;
	
	@Column(name = "end_date")
	private Date endDate;
	
	@Column( name = "creation_date", insertable = false, updatable = false)
	private Date creationDate;
	
	@Column( name = "last_modified_date", insertable = false, updatable = false)
	private Date lastModifiedDate;
	
	@OneToMany( fetch = FetchType.LAZY, mappedBy = "queryEndpointId"  )
	@Column( name = "query_endpoint_map_id", nullable = false  )
	private Set<PatientRecordEntity> results = new HashSet<PatientRecordEntity>();
	
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

	public QueryEndpointStatusEntity getStatus() {
		return status;
	}

	public void setStatus(QueryEndpointStatusEntity status) {
		this.status = status;
	}

	public Long getQueryId() {
		return queryId;
	}

	public void setQueryId(Long queryId) {
		this.queryId = queryId;
	}

	public Long getEndpointId() {
		return endpointId;
	}

	public void setEndpointId(Long endpointId) {
		this.endpointId = endpointId;
	}

	public EndpointEntity getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(EndpointEntity endpoint) {
		this.endpoint = endpoint;
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

	public Set<PatientRecordEntity> getResults() {
		return results;
	}

	public void setResults(Set<PatientRecordEntity> results) {
		this.results = results;
	}

	public Long getStatusId() {
		return statusId;
	}

	public void setStatusId(Long statusId) {
		this.statusId = statusId;
	}
}
