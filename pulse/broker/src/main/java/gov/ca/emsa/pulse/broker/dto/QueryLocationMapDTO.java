package gov.ca.emsa.pulse.broker.dto;

import gov.ca.emsa.pulse.broker.entity.PatientRecordEntity;
import gov.ca.emsa.pulse.broker.entity.QueryLocationMapEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import gov.ca.emsa.pulse.common.domain.QueryLocationStatus;

public class QueryLocationMapDTO {

	private Long id;
	private Long queryId;
	private Long locationId;
	private LocationDTO location;
	private QueryLocationStatus status;
	private Date startDate;
	private Date endDate;
	private List<PatientRecordDTO> results;
	
	public QueryLocationMapDTO(){
		results = new ArrayList<PatientRecordDTO>();
	}
	
	public QueryLocationMapDTO(QueryLocationMapEntity entity)
	{
		this();
		if(entity != null) {
			this.id = entity.getId();
			this.queryId = entity.getQueryId();
			this.locationId = entity.getLocationId();
			if(entity.getLocation() != null) {
				this.location = new LocationDTO(entity.getLocation());
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

	public Long getLocationId() {
		return locationId;
	}

	public void setLocationId(Long locationId) {
		this.locationId = locationId;
	}

	public QueryLocationStatus getStatus() {
		return status;
	}

	public void setStatus(QueryLocationStatus status) {
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

	public LocationDTO getLocation() {
		return location;
	}

	public void setLocation(LocationDTO location) {
		this.location = location;
	}
}