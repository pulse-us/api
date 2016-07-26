package gov.ca.emsa.pulse.common.domain;

import java.util.ArrayList;
import java.util.List;

public class Query {
	private Long id;
	private String userToken;
	private String status;
	private PatientSearch terms;
	private List<QueryOrganization> orgStatuses;

	public Query() {
		orgStatuses = new ArrayList<QueryOrganization>();
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

	public PatientSearch getTerms() {
		return terms;
	}

	public void setTerms(PatientSearch terms) {
		this.terms = terms;
	}

	public List<QueryOrganization> getOrgStatuses() {
		return orgStatuses;
	}

	public void setOrgStatuses(List<QueryOrganization> orgStatuses) {
		this.orgStatuses = orgStatuses;
	}
}
