package gov.ca.emsa.pulse.common.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Query {
	private Long id;
	private String userToken;
	private String status;
	private PatientSearch terms;
	private Date lastRead;
	private List<QueryLocationMap> locationStatuses;

	public Query() {
		locationStatuses = new ArrayList<QueryLocationMap>();
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

	public List<QueryLocationMap> getLocationStatuses() {
		return locationStatuses;
	}

	public void setLocationStatuses(List<QueryLocationMap> locationStatuses) {
		this.locationStatuses = locationStatuses;
	}

	public Date getLastRead() {
		return lastRead;
	}

	public void setLastRead(Date lastRead) {
		this.lastRead = lastRead;
	}
}
