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
@Table(name="location_endpoint_map")
public class LocationEndpointMapEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column( name = "id", nullable = false )
	private Long id;
	
	@Basic( optional = false )
	@Column( name = "location_id", nullable = false )
	private Long locationId;
	
	@OneToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "location_id", unique=true, nullable = true, insertable=false, updatable= false)
	private LocationEntity location;
	
	@Basic( optional = false )
	@Column( name = "endpoint_id", nullable = false )
	private Long endpointId;
	
	@OneToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "endpoint_id", unique=true, nullable = true, insertable=false, updatable= false)
	private EndpointEntity endpoint;
	
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

	public Long getLocationId() {
		return locationId;
	}

	public void setLocationId(Long locationId) {
		this.locationId = locationId;
	}

	public LocationEntity getLocation() {
		return location;
	}

	public void setLocation(LocationEntity location) {
		this.location = location;
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
