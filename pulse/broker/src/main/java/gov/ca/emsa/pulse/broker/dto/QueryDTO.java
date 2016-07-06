package gov.ca.emsa.pulse.broker.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import gov.ca.emsa.pulse.broker.entity.QueryEntity;
import gov.ca.emsa.pulse.broker.entity.QueryOrganizationEntity;

public class QueryDTO {

	private Long id;
	private String userId;
	private String status;
	private String terms;
	private Date lastReadDate;
	private List<QueryOrganizationDTO> orgStatuses;
	
	public QueryDTO(){
		orgStatuses = new ArrayList<QueryOrganizationDTO>();
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
			
			if(entity.getOrgStatuses() != null && entity.getOrgStatuses().size() > 0) {
				for(QueryOrganizationEntity orgStatus : entity.getOrgStatuses()) {
					QueryOrganizationDTO dto = new QueryOrganizationDTO(orgStatus);
					orgStatuses.add(dto);
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

	public List<QueryOrganizationDTO> getOrgStatuses() {
		return orgStatuses;
	}

	public void setOrgStatuses(List<QueryOrganizationDTO> orgStatuses) {
		this.orgStatuses = orgStatuses;
	}

	public Date getLastReadDate() {
		return lastReadDate;
	}

	public void setLastReadDate(Date lastReadDate) {
		this.lastReadDate = lastReadDate;
	}
}