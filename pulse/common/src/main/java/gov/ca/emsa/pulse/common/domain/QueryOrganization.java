package gov.ca.emsa.pulse.common.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class QueryOrganization {
	private Long id;
	private Long queryId;
	private Organization org;
	private String status;
	private Date startDate;
	private Date endDate;
	private Boolean success;
	private List<PatientRecord> results;
	
	public QueryOrganization() {
		results = new ArrayList<PatientRecord>();
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public List<PatientRecord> getResults() {
		return results;
	}

	public void setResults(List<PatientRecord> results) {
		this.results = results;
	}

	public Organization getOrg() {
		return org;
	}

	public void setOrg(Organization org) {
		this.org = org;
	}
}
