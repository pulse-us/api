package gov.ca.emsa.pulse.broker.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.postgresql.util.PGInterval;

@Entity
public class PatientDiscoveryRequestStatisticsEntity {
	@Id
	@Column(name = "organization_id", insertable = false, updatable = false)
	private Long organizationId;
	
	@Column(name = "organization_name", insertable = false, updatable = false)
	private String organizationName;
	
	@Column(name = "organization_adapter", insertable = false, updatable = false)
	private String orgAdapter;
	
	@Column(name = "organization_is_active", insertable = false, updatable = false)
	private Boolean orgIsActive;
	
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

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public String getOrganizationName() {
		return organizationName;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
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

	public String getOrgAdapter() {
		return orgAdapter;
	}

	public void setOrgAdapter(String orgAdapter) {
		this.orgAdapter = orgAdapter;
	}

	public Boolean getOrgIsActive() {
		return orgIsActive;
	}

	public void setOrgIsActive(Boolean orgIsActive) {
		this.orgIsActive = orgIsActive;
	}
}