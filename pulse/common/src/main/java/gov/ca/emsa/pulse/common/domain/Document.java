package gov.ca.emsa.pulse.common.domain;

import javax.xml.bind.annotation.XmlTransient;

public class Document {
	private String id;
	private Boolean cached;
	private Long locationMapId;
	private Patient patient;
	private QueryLocationStatus status;
	
	//metadata
	private String className;
	private String confidentiality;
	private String format;
	private String name;
	private String description;
	private String size;
	private String creationTime;
	
	//required to get the document back later
	//identifies a document uniquely to a location
	private DocumentIdentifier identifier;
		
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
	public Long getLocationMapId() {
		return locationMapId;
	}
	public void setLocationMapId(Long locationMapId) {
		this.locationMapId = locationMapId;
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

	@XmlTransient
	public DocumentIdentifier getIdentifier() {
		return identifier;
	}

	public void setIdentifier(DocumentIdentifier identifier) {
		this.identifier = identifier;
	}

	public QueryLocationStatus getStatus() {
		return status;
	}

	public void setStatus(QueryLocationStatus status) {
		this.status = status;
	}
	
}
