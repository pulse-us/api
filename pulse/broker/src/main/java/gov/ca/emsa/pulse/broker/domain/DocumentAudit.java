package gov.ca.emsa.pulse.broker.domain;

import gov.ca.emsa.pulse.common.domain.DocumentIdentifier;

public class DocumentAudit {
	
	private DocumentIdentifier documentIdentifier;
	private String format;
	private String name;
	private String className;
	private String confidentiality;
	private String description;
	private String size;
	private String creationTime;
	
	
	public DocumentIdentifier getDocumentIdentifier() {
		return documentIdentifier;
	}
	public void setDocumentIdentifier(DocumentIdentifier documentIdentifier) {
		this.documentIdentifier = documentIdentifier;
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	
	

}
