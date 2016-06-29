package gov.ca.emsa.pulse.broker.domain;

import gov.ca.emsa.pulse.broker.dto.QueryOrganizationDTO;

public class QueryOrganizationStatus {
	private Long id;
	private Long queryId;
	private Long orgId;
	private String status;
	private Long startDate;
	private Long endDate;
	private Boolean fromCache;
	private Boolean success;
	
	public QueryOrganizationStatus(QueryOrganizationDTO dto) {
		this.id = dto.getId();
		this.queryId = dto.getQueryId();
		this.orgId = dto.getOrgId();
		this.status = dto.getStatus();
		this.fromCache = dto.getFromCache();
		this.success = dto.getSuccess();
		if(dto.getStartDate() != null) {
			this.startDate = dto.getStartDate().getTime();
		}
		if(dto.getEndDate() != null) {
			this.endDate = dto.getEndDate().getTime();
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getQueryId() {
		return queryId;
	}

	public void setQueryId(Long queryId) {
		this.queryId = queryId;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getStartDate() {
		return startDate;
	}

	public void setStartDate(Long startDate) {
		this.startDate = startDate;
	}

	public Long getEndDate() {
		return endDate;
	}

	public void setEndDate(Long endDate) {
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
