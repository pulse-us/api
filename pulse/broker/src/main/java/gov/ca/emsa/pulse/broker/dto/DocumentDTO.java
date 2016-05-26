package gov.ca.emsa.pulse.broker.dto;

import java.util.Date;

import gov.ca.emsa.pulse.broker.entity.DocumentEntity;
import gov.ca.emsa.pulse.broker.entity.PatientEntity;

public class DocumentDTO {
	private long id;
	private String name;
	private String format;
	private PatientDTO patient;
	
	public DocumentDTO() {
	}
	
	public DocumentDTO(DocumentEntity entity) {
		this.id = entity.getId();
		this.name = entity.getName();
		this.format = entity.getFormat();
		if(entity.getPatient() != null) {
			this.patient = new PatientDTO(entity.getPatient());
		}
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

	public PatientDTO getPatient() {
		return patient;
	}

	public void setPatient(PatientDTO patient) {
		this.patient = patient;
	}
}
