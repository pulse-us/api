package gov.ca.emsa.pulse.common.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Query {
	private Long id;
	private String userToken;
	private QueryStatus status;
	private PatientSearch terms;
	private Date lastRead;
	private List<QueryEndpointMap> endpointStatuses;

	public Query() {
		endpointStatuses = new ArrayList<QueryEndpointMap>();
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

	public QueryStatus getStatus() {
		return status;
	}

	public void setStatus(QueryStatus status) {
		this.status = status;
	}

	public PatientSearch getTerms() {
		return terms;
	}

	public void setTerms(PatientSearch terms) {
		this.terms = terms;
	}

	public List<QueryEndpointMap> getEndpointStatuses() {
		return endpointStatuses;
	}

	public void setEndpointStatuses(List<QueryEndpointMap> endpointStatuses) {
		this.endpointStatuses = endpointStatuses;
	}

	public Date getLastRead() {
		return lastRead;
	}

	public void setLastRead(Date lastRead) {
		this.lastRead = lastRead;
	}
}
