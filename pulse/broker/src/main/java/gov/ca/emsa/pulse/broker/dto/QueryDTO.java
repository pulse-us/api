package gov.ca.emsa.pulse.broker.dto;

import gov.ca.emsa.pulse.broker.entity.QueryEntity;
import gov.ca.emsa.pulse.broker.entity.QueryLocationMapEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class QueryDTO {

	private Long id;
	private String userId;
	private String status;
	private String terms;
	private Date lastReadDate;
	private List<QueryLocationMapDTO> locationStatuses;
	
	public QueryDTO(){
		locationStatuses = new ArrayList<QueryLocationMapDTO>();
	}
	
	public QueryDTO(QueryEntity entity)
	{
		this();
		if(entity != null) {
			this.id = entity.getId();
			this.userId = entity.getUserId();
			this.status = entity.getStatus();
			this.terms = entity.getTerms();
			this.lastReadDate = entity.getLastReadDate();
			
			if(entity.getLocationStatuses() != null && entity.getLocationStatuses().size() > 0) {
				for(QueryLocationMapEntity locationStatus : entity.getLocationStatuses()) {
					QueryLocationMapDTO dto = new QueryLocationMapDTO(locationStatus);
					locationStatuses.add(dto);
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTerms() {
		return terms;
	}

	public void setTerms(String terms) {
		this.terms = terms;
	}

	public List<QueryLocationMapDTO> getLocationStatuses() {
		return locationStatuses;
	}

	public void setLocationStatuses(List<QueryLocationMapDTO> locationStatuses) {
		this.locationStatuses = locationStatuses;
	}

	public Date getLastReadDate() {
		return lastReadDate;
	}

	public void setLastReadDate(Date lastReadDate) {
		this.lastReadDate = lastReadDate;
	}
}