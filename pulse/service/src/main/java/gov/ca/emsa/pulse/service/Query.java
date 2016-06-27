package gov.ca.emsa.pulse.service;

import java.util.ArrayList;
import java.util.List;

public class Query {
	private Long id;
	private String userToken;
	private String status;
	private String terms;
	private List<OrganizationStatus> orgStatuses;

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

	public List<OrganizationStatus> getOrgStatuses() {
		return orgStatuses;
	}

	public void setOrgStatuses(List<OrganizationStatus> orgStatuses) {
		this.orgStatuses = orgStatuses;
	}
}
