package gov.ca.emsa.pulse.broker.dto;

import gov.ca.emsa.pulse.broker.entity.DocumentEntity;

public class DocumentDTO {
	private long id;
	private byte[] contents;
	private Long patientLocationMapId;
	
	//metadata
	private String format;
	private String name;
	private String className;
	private String confidentiality;
	private String description;
	private String size;
	private String creationTime;
		
	//required to get the document back later
	private String homeCommunityId; //64 chars
	private String repositoryUniqueId;
	private String documentUniqueId;
		
	public DocumentDTO() {
	}
	
	public DocumentDTO(DocumentEntity entity) {
		this.id = entity.getId();
		this.name = entity.getName();
		this.format = entity.getFormat();
		this.className = entity.getClassName();
		this.confidentiality = entity.getConfidentiality();
		this.description = entity.getDescription();
		this.size = entity.getSize();
		this.creationTime = entity.getCreationTime();
		this.homeCommunityId = entity.getHomeCommunityId();
		this.repositoryUniqueId = entity.getRepositoryUniqueId();
		this.documentUniqueId = entity.getDocumentUniqueId();
		this.contents = entity.getContents();
		this.patientLocationMapId = entity.getPatientLocationMapId();
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public byte[] getContents() {
		return contents;
	}

	public void setContents(byte[] contents) {
		this.contents = contents;
	}

	public Long getPatientLocationMapId() {
		return patientLocationMapId;
	}

	public void setPatientLocationMapId(Long patientLocationMapId) {
		this.patientLocationMapId = patientLocationMapId;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(String creationTime) {
		this.creationTime = creationTime;
	}

	public String getHomeCommunityId() {
		return homeCommunityId;
	}

	public void setHomeCommunityId(String homeCommunityId) {
		this.homeCommunityId = homeCommunityId;
	}

	public String getRepositoryUniqueId() {
		return repositoryUniqueId;
	}

	public void setRepositoryUniqueId(String repositoryUniqueId) {
		this.repositoryUniqueId = repositoryUniqueId;
	}

	public String getDocumentUniqueId() {
		return documentUniqueId;
	}

	public void setDocumentUniqueId(String documentUniqueId) {
		this.documentUniqueId = documentUniqueId;
	}
}
