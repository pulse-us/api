package gov.ca.emsa.pulse.broker.dto;

import gov.ca.emsa.pulse.broker.entity.AuditEntity;
import java.util.Date;

public class AuditDTO {
	
	private Long id;
	
	private String queryType;
	
	private String query;
	
	private String querent;
	
	private Date creationDate;
	
	private Date lastModifiedDate;
	
	public AuditDTO() {
	}
	
	public AuditDTO(AuditEntity entity) {
		this.id = entity.getId();
		this.queryType = entity.getQueryType();
		this.query = entity.getQuery();
		this.querent = entity.getQuerent();
		this.creationDate = entity.getCreationDate();
		this.lastModifiedDate = entity.getLastModifiedDate();
	}
	
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

	public String getQueryType() {
		return queryType;
	}

	public void setQueryType(String queryType) {
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
