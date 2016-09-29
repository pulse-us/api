package gov.ca.emsa.pulse.common.domain;

import javax.xml.bind.annotation.XmlTransient;

public class Document {
	private String id;
	private Boolean cached;
	private Long orgMapId;
	private Patient patient;
	
	//metadata
	private String className;
	private String confidentiality;
	private String format;
	private String name;
	private String description;
	private String size;
	private String creationTime;
	
	//required to get the document back later
	private String homeCommunityId; //64 chars
	private String repositoryUniqueId;
	private String documentUniqueId;
		
	public Document() {}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getOrgMapId() {
		return orgMapId;
	}
	public void setOrgMapId(Long orgMapId) {
		this.orgMapId = orgMapId;
	}

	public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	public Boolean getCached() {
		return cached;
	}

	public void setCached(Boolean cached) {
		this.cached = cached;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	@XmlTransient
	public String getHomeCommunityId() {
		return homeCommunityId;
	}

	public void setHomeCommunityId(String homeCommunityId) {
		this.homeCommunityId = homeCommunityId;
	}

	public String getRepositoryUniqueId() {
		return repositoryUniqueId;
	}

	@XmlTransient
	public void setRepositoryUniqueId(String repositoryUniqueId) {
		this.repositoryUniqueId = repositoryUniqueId;
	}

	public String getDocumentUniqueId() {
		return documentUniqueId;
	}

	@XmlTransient
	public void setDocumentUniqueId(String documentUniqueId) {
		this.documentUniqueId = documentUniqueId;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getConfidentiality() {
		return confidentiality;
	}

	public void setConfidentiality(String confidentiality) {
		this.confidentiality = confidentiality;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(String creationTime) {
		this.creationTime = creationTime;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}
