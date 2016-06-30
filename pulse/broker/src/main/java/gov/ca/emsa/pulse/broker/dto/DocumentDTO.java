package gov.ca.emsa.pulse.broker.dto;

import gov.ca.emsa.pulse.broker.entity.DocumentEntity;

public class DocumentDTO {
	private long id;
	private String name;
	private String format;
	private byte[] contents;
	private Long patientOrgMapId;
	
	public DocumentDTO() {
	}
	
	public DocumentDTO(DocumentEntity entity) {
		this.id = entity.getId();
		this.name = entity.getName();
		this.format = entity.getFormat();
		this.contents = entity.getContents();
		this.patientOrgMapId = entity.getPatientOrgMapId();
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

	public Long getPatientOrgMapId() {
		return patientOrgMapId;
	}

	public void setPatientOrgMapId(Long patientOrgMapId) {
		this.patientOrgMapId = patientOrgMapId;
	}
}
