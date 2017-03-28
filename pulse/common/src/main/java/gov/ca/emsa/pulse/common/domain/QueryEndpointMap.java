package gov.ca.emsa.pulse.common.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class QueryEndpointMap {
	private Long id;
	private Long queryId;
	private Endpoint endpoint;
	private QueryEndpointStatus status;
	private Date startDate;
	private Date endDate;
	private List<PatientRecord> results;
	
	public QueryEndpointMap() {
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

	public QueryEndpointStatus getStatus() {
		return status;
	}

	public void setStatus(QueryEndpointStatus status) {
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

	public List<PatientRecord> getResults() {
		return results;
	}

	public void setResults(List<PatientRecord> results) {
		this.results = results;
	}

	public Endpoint getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(Endpoint endpoint) {
		this.endpoint = endpoint;
	}
}
