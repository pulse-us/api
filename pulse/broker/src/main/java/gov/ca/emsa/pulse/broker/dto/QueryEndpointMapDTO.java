package gov.ca.emsa.pulse.broker.dto;

import gov.ca.emsa.pulse.broker.entity.PatientRecordEntity;
import gov.ca.emsa.pulse.broker.entity.QueryEndpointMapEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import gov.ca.emsa.pulse.common.domain.QueryEndpointStatus;

public class QueryEndpointMapDTO {

	private Long id;
	private Long queryId;
	private Long endpointId;
	private EndpointDTO endpoint;
	private QueryEndpointStatus status;
	private Date startDate;
	private Date endDate;
	private List<PatientRecordDTO> results;
	
	public QueryEndpointMapDTO(){
		results = new ArrayList<PatientRecordDTO>();
	}
	
	public QueryEndpointMapDTO(QueryEndpointMapEntity entity)
	{
		this();
		if(entity != null) {
			this.id = entity.getId();
			this.queryId = entity.getQueryId();
			this.endpointId = entity.getEndpointId();
			if(entity.getEndpoint() != null) {
				this.endpoint = new EndpointDTO(entity.getEndpoint());
			}
			
			if(entity.getStatus() != null) {
				this.status = entity.getStatus().getStatus();
			}
			this.startDate = entity.getStartDate();
			this.endDate = entity.getEndDate();
			
			if(entity.getResults() != null && entity.getResults().size() > 0) {
				for(PatientRecordEntity pr : entity.getResults()) {
					PatientRecordDTO dto = new PatientRecordDTO(pr);
					this.results.add(dto);
				}
			}
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

	public Long getEndpointId() {
		return endpointId;
	}

	public void setEndpointId(Long endpointId) {
		this.endpointId = endpointId;
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

	public List<PatientRecordDTO> getResults() {
		return results;
	}

	public void setResults(List<PatientRecordDTO> results) {
		this.results = results;
	}

	public EndpointDTO getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(EndpointDTO endpoint) {
		this.endpoint = endpoint;
	}
}