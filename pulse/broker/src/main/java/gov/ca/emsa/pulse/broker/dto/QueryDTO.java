package gov.ca.emsa.pulse.broker.dto;

import java.util.ArrayList;
import java.util.List;

import gov.ca.emsa.pulse.broker.entity.QueryEntity;
import gov.ca.emsa.pulse.broker.entity.QueryOrganizationStatusMap;

public class QueryDTO {

	private Long id;
	private String userToken;
	private String status;
	private String terms;
	private List<QueryOrganizationDTO> orgStatuses;
	
	public QueryDTO(){
		orgStatuses = new ArrayList<QueryOrganizationDTO>();
	}
	
	public QueryDTO(QueryEntity entity)
	{
		super();
		if(entity != null) {
			this.id = entity.getId();
			this.userToken = entity.getUserToken();
			this.status = entity.getStatus();
			this.terms = entity.getTerms();
			
			if(entity.getOrgStatuses() != null && entity.getOrgStatuses().size() > 0) {
				for(QueryOrganizationStatusMap orgStatus : entity.getOrgStatuses()) {
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

	public String getUserToken() {
		return userToken;
	}

	public void setUserToken(String userToken) {
		this.userToken = userToken;
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
}
