package gov.ca.emsa.pulse.broker.domain;

import java.util.Date;

public class Audit {
	private Long id;
	
	private QueryType queryType;
	
	private String query;
	
	private String querent;
	
	private Date creationDate;
	
	private Date lastModifiedDate;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public QueryType getQueryType() {
		return queryType;
	}

	public void setQueryType(QueryType queryType) {
		this.queryType = queryType;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String getQuerent() {
		return querent;
	}

	public void setQuerent(String querent) {
		this.querent = querent;
	}
}
