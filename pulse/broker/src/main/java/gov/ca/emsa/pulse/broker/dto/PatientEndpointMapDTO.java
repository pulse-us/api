package gov.ca.emsa.pulse.broker.dto;

import gov.ca.emsa.pulse.broker.entity.DocumentEntity;
import gov.ca.emsa.pulse.broker.entity.PatientEndpointMapEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import gov.ca.emsa.pulse.common.domain.QueryEndpointStatus;

public class PatientEndpointMapDTO {
	private Long id;
	private Long patientId;
	private String homeCommunityId;
	private String externalPatientRecordId; // PID + AA
	private QueryEndpointStatus documentsQueryStatus;
	private Long endpointId;
	private EndpointDTO endpoint;
	private Date documentsQueryStart;
	private Date documentsQueryEnd;
	private List<DocumentDTO> documents;
		
	public PatientEndpointMapDTO() {
		documents = new ArrayList<DocumentDTO>();
	}
	
	public PatientEndpointMapDTO(PatientEndpointMapEntity entity) {
		this();
		this.id = entity.getId();
		this.patientId = entity.getPatientId();
		this.endpointId = entity.getEndpointId();
		if(entity.getEndpoint() != null) {
			this.endpoint = new EndpointDTO(entity.getEndpoint());
		}
		this.homeCommunityId = entity.getHomeCommunityId();
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
	public Long getEndpointId() {
		return endpointId;
	}
	public void setEndpointId(Long endpointId) {
		this.endpointId = endpointId;
	}
	
	public String getHomeCommunityId() {
		return homeCommunityId;
	}

	public void setHomeCommunityId(String homeCommunityId) {
		this.homeCommunityId = homeCommunityId;
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

	public EndpointDTO getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(EndpointDTO endpoint) {
		this.endpoint = endpoint;
	}

	public List<DocumentDTO> getDocuments() {
		return documents;
	}

	public void setDocuments(List<DocumentDTO> documents) {
		this.documents = documents;
	}

	public QueryEndpointStatus getDocumentsQueryStatus() {
		return documentsQueryStatus;
	}

	public void setDocumentsQueryStatus(QueryEndpointStatus status) {
		this.documentsQueryStatus = status;
	}
}
