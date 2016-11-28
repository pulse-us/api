package gov.ca.emsa.pulse.broker.dto;

import gov.ca.emsa.pulse.broker.entity.DocumentEntity;
import gov.ca.emsa.pulse.broker.entity.PatientLocationMapEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import gov.ca.emsa.pulse.common.domain.QueryLocationStatus;

public class PatientLocationMapDTO {
	private Long id;
	private Long patientId;
	private Long locationId;
	private LocationDTO location;
	private String externalPatientRecordId;
	private QueryLocationStatus documentsQueryStatus;
	private Date documentsQueryStart;
	private Date documentsQueryEnd;
	private List<DocumentDTO> documents;
	
	public PatientLocationMapDTO() {
		documents = new ArrayList<DocumentDTO>();
	}
	
	public PatientLocationMapDTO(PatientLocationMapEntity entity) {
		this();
		this.id = entity.getId();
		this.patientId = entity.getPatientId();
		this.locationId = entity.getLocationId();
		if(entity.getLocation() != null) {
			this.location = new LocationDTO(entity.getLocation());
		}
		this.externalPatientRecordId = entity.getExternalPatientRecordId();
		if(entity.getStatus() != null) {
			this.documentsQueryStatus = entity.getStatus().getStatus();
		}
		this.documentsQueryStart = entity.getDocumentsQueryStart();
		this.documentsQueryEnd = entity.getDocumentsQueryEnd();
		if(entity.getDocuments() != null && entity.getDocuments().size() > 0) {
			for(DocumentEntity doc : entity.getDocuments()) {
				DocumentDTO docDto = new DocumentDTO(doc);
				this.documents.add(docDto);
			}
		}
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getPatientId() {
		return patientId;
	}
	public void setPatientId(Long patientRecordId) {
		this.patientId = patientRecordId;
	}
	public Long getLocationId() {
		return locationId;
	}
	public void setLocationId(Long locationId) {
		this.locationId = locationId;
	}
	public String getExternalPatientRecordId() {
		return externalPatientRecordId;
	}
	public void setExternalPatientRecordId(String externalPatientRecordId) {
		this.externalPatientRecordId = externalPatientRecordId;
	}
	public Date getDocumentsQueryStart() {
		return documentsQueryStart;
	}
	public void setDocumentsQueryStart(Date documentsQueryStart) {
		this.documentsQueryStart = documentsQueryStart;
	}
	public Date getDocumentsQueryEnd() {
		return documentsQueryEnd;
	}
	public void setDocumentsQueryEnd(Date documentsQueryEnd) {
		this.documentsQueryEnd = documentsQueryEnd;
	}

	public LocationDTO getLocation() {
		return location;
	}

	public void setLocation(LocationDTO location) {
		this.location = location;
	}

	public List<DocumentDTO> getDocuments() {
		return documents;
	}

	public void setDocuments(List<DocumentDTO> documents) {
		this.documents = documents;
	}

	public QueryLocationStatus getDocumentsQueryStatus() {
		return documentsQueryStatus;
	}

	public void setDocumentsQueryStatus(QueryLocationStatus status) {
		this.documentsQueryStatus = status;
	}
}
