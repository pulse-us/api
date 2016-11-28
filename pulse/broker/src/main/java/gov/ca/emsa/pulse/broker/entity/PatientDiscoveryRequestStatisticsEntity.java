package gov.ca.emsa.pulse.broker.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.postgresql.util.PGInterval;

@Entity
public class PatientDiscoveryRequestStatisticsEntity {
	@Id
	@Column(name = "location_id", insertable = false, updatable = false)
	private Long locationId;
	
	@Column(name = "location_name", insertable = false, updatable = false)
	private String locationName;
	
	@Column(name = "location_type", insertable = false, updatable = false)
	private String locationType;
	
	//is the location active or whatever
	@Column(name = "location_status_name", insertable = false, updatable = false)
	private String locationStatus;
	
	@Column(name = "total_request_count", insertable = false, updatable = false)
	private Long totalRequestCount;
	
	@Column(name = "total_request_average_seconds", insertable = false, updatable = false)
	private Double totalRequestAverageSeconds;
	
	@Column(name = "successful_request_count", insertable = false, updatable = false)
	private Long successfulRequestCount;
	
	@Column(name = "successful_request_average_seconds", insertable = false, updatable = false)
	private Double successfulRequestAverageSeconds;
	
	@Column(name = "failed_request_count", insertable = false, updatable = false)
	private Long failedRequestCount;
	
	@Column(name = "failed_request_average_seconds", insertable = false, updatable = false)
	private Double failedRequestAverageSeconds;
	
	@Column(name = "cancelled_request_count", insertable = false, updatable = false)
	private Long cancelledRequestCount;
	
	@Column(name = "cancelled_request_average_seconds", insertable = false, updatable = false)
	private Double cancelledRequestAverageSeconds;

	public Long getLocationId() {
		return locationId;
	}

	public void setLocationId(Long locationId) {
		this.locationId = locationId;
	}

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public Long getTotalRequestCount() {
		return totalRequestCount;
	}

	public void setTotalRequestCount(Long totalRequestCount) {
		this.totalRequestCount = totalRequestCount;
	}

	public Double getTotalRequestAverageSeconds() {
		return totalRequestAverageSeconds;
	}

	public void setTotalRequestAverageSeconds(Double totalRequestAverageSeconds) {
		this.totalRequestAverageSeconds = totalRequestAverageSeconds;
	}

	public Long getSuccessfulRequestCount() {
		return successfulRequestCount;
	}

	public void setSuccessfulRequestCount(Long successfulRequestCount) {
		this.successfulRequestCount = successfulRequestCount;
	}

	public Double getSuccessfulRequestAverageSeconds() {
		return successfulRequestAverageSeconds;
	}

	public void setSuccessfulRequestAverageSeconds(Double successfulRequestAverageSeconds) {
		this.successfulRequestAverageSeconds = successfulRequestAverageSeconds;
	}

	public Long getFailedRequestCount() {
		return failedRequestCount;
	}

	public void setFailedRequestCount(Long failedRequestCount) {
		this.failedRequestCount = failedRequestCount;
	}

	public Double getFailedRequestAverageSeconds() {
		return failedRequestAverageSeconds;
	}

	public void setFailedRequestAverageSeconds(Double failedRequestAverageSeconds) {
		this.failedRequestAverageSeconds = failedRequestAverageSeconds;
	}

	public Long getCancelledRequestCount() {
		return cancelledRequestCount;
	}

	public void setCancelledRequestCount(Long cancelledRequestCount) {
		this.cancelledRequestCount = cancelledRequestCount;
	}

	public Double getCancelledRequestAverageSeconds() {
		return cancelledRequestAverageSeconds;
	}

	public void setCancelledRequestAverageSeconds(Double cancelledRequestAverageSeconds) {
		this.cancelledRequestAverageSeconds = cancelledRequestAverageSeconds;
	}

	public String getLocationType() {
		return locationType;
	}

	public void setLocationType(String locationType) {
		this.locationType = locationType;
	}

	public String getLocationStatus() {
		return locationStatus;
	}

	public void setLocationStatus(String locationStatus) {
		this.locationStatus = locationStatus;
	}
}