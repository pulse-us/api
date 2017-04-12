package gov.ca.emsa.pulse.broker.dto;

import gov.ca.emsa.pulse.broker.entity.DocumentEntity;
import gov.ca.emsa.pulse.common.domain.QueryEndpointStatus;

public class DocumentDTO {
	private long id;
	private String contents;
	private Long patientEndpointMapId;
	private Long statusId;
	private QueryEndpointStatus status;
	
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
		this.statusId = entity.getStatusId();
		if(entity.getStatus() != null) {
			this.status = entity.getStatus().getStatus();
		}
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
		this.patientEndpointMapId = entity.getPatientEndpointMapId();
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

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public Long getPatientEndpointMapId() {
		return patientEndpointMapId;
	}

	public void setPatientEndpointMapId(Long patientEndpointMapId) {
		this.patientEndpointMapId = patientEndpointMapId;
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

	public Long getStatusId() {
		return statusId;
	}

	public void setStatusId(Long statusId) {
		this.statusId = statusId;
	}

	public QueryEndpointStatus getStatus() {
		return status;
	}

	public void setStatus(QueryEndpointStatus status) {
		this.status = status;
	}
}
