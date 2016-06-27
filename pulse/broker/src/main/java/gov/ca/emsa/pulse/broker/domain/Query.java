package gov.ca.emsa.pulse.broker.domain;

import java.util.ArrayList;
import java.util.List;

import gov.ca.emsa.pulse.broker.dto.QueryDTO;
import gov.ca.emsa.pulse.broker.dto.QueryOrganizationDTO;

public class Query {
	private Long id;
	private String userToken;
	private String status;
	private String terms;
	private List<QueryOrganizationStatus> orgStatuses;
	
	public Query(QueryDTO dto) {
		this.id = dto.getId();
		this.userToken = dto.getUserToken();
		this.status = dto.getStatus();
		this.terms = dto.getTerms();
		this.orgStatuses = new ArrayList<QueryOrganizationStatus>();
		if(dto.getOrgStatuses() != null && dto.getOrgStatuses().size() > 0) {
			for(QueryOrganizationDTO orgDto : dto.getOrgStatuses()) {
				this.orgStatuses.add(new QueryOrganizationStatus(orgDto));
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

	public List<QueryOrganizationStatus> getOrgStatuses() {
		return orgStatuses;
	}

	public void setOrgStatuses(List<QueryOrganizationStatus> orgStatuses) {
		this.orgStatuses = orgStatuses;
	}
}
