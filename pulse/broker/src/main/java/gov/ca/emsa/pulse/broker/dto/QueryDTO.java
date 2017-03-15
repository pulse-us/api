package gov.ca.emsa.pulse.broker.dto;

import gov.ca.emsa.pulse.broker.entity.QueryEntity;
import gov.ca.emsa.pulse.broker.entity.QueryEndpointMapEntity;
import gov.ca.emsa.pulse.common.domain.QueryEndpointStatus;
import gov.ca.emsa.pulse.common.domain.QueryStatus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class QueryDTO {

	private Long id;
	private String userId;
	private QueryStatus status;
	private String terms;
	private Date lastReadDate;
	private List<QueryEndpointMapDTO> endpointStatuses;
	
	public QueryDTO(){
		endpointStatuses = new ArrayList<QueryEndpointMapDTO>();
	}
	
	public QueryDTO(QueryEntity entity)
	{
		this();
		if(entity != null) {
			this.id = entity.getId();
			this.userId = entity.getUserId();
			if(entity.getStatus() != null) {
				this.status = entity.getStatus().getStatus();
			}
			this.terms = entity.getTerms();
			this.lastReadDate = entity.getLastReadDate();
			
			if(entity.getEndpointStatuses() != null && entity.getEndpointStatuses().size() > 0) {
				for(QueryEndpointMapEntity locationStatus : entity.getEndpointStatuses()) {
					QueryEndpointMapDTO dto = new QueryEndpointMapDTO(locationStatus);
					endpointStatuses.add(dto);
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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public QueryStatus getStatus() {
		return status;
	}

	public void setStatus(QueryStatus status) {
		this.status = status;
	}

	public String getTerms() {
		return terms;
	}

	public void setTerms(String terms) {
		this.terms = terms;
	}

	public List<QueryEndpointMapDTO> getEndpointStatuses() {
		return endpointStatuses;
	}

	public void setEndpointStatuses(List<QueryEndpointMapDTO> endpointStatuses) {
		this.endpointStatuses = endpointStatuses;
	}

	public Date getLastReadDate() {
		return lastReadDate;
	}

	public void setLastReadDate(Date lastReadDate) {
		this.lastReadDate = lastReadDate;
	}
}