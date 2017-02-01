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

import org.hibernate.annotations.Where;

@Entity
@Table(name="query")
public class QueryEntity {
	
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column( name = "id", nullable = false )
	private Long id;
	
	@Column(name="user_id")
	private String userId;
	
	@Column(name = "query_status_id")
	private Long statusId;
	
	@Basic( optional = true )
	@OneToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "query_status_id", unique=true, nullable = false, insertable=false, updatable= false)
	private QueryStatusEntity status;
	
	@Column(name = "terms")
	private String terms;
	
	@Column( name = "last_read_date")
	private Date lastReadDate;
	
	@Column( name = "creation_date", insertable = false, updatable = false)
	private Date creationDate;
	
	@Column( name = "last_modified_date", insertable = false, updatable = false)
	private Date lastModifiedDate;
	
 	@OneToMany( fetch = FetchType.LAZY, mappedBy = "queryId"  )
	@Column( name = "query_id", nullable = false  )
 	@Where(clause="query_location_status_id != 5")
	private Set<QueryLocationMapEntity> locationStatuses = new HashSet<QueryLocationMapEntity>();
	
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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getTerms() {
		return terms;
	}

	public void setTerms(String terms) {
		this.terms = terms;
	}

	public Set<QueryLocationMapEntity> getLocationStatuses() {
		return locationStatuses;
	}

	public void setLocationStatuses(Set<QueryLocationMapEntity> locationStatuses) {
		this.locationStatuses = locationStatuses;
	}

	public Date getLastReadDate() {
		return lastReadDate;
	}

	public void setLastReadDate(Date lastReadDate) {
		this.lastReadDate = lastReadDate;
	}

	public Long getStatusId() {
		return statusId;
	}

	public void setStatusId(Long statusId) {
		this.statusId = statusId;
	}

	public QueryStatusEntity getStatus() {
		return status;
	}

	public void setStatus(QueryStatusEntity status) {
		this.status = status;
	}
}