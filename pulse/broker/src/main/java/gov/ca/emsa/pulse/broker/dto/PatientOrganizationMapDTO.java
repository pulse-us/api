package gov.ca.emsa.pulse.broker.dto;

import gov.ca.emsa.pulse.broker.entity.DocumentEntity;
import gov.ca.emsa.pulse.broker.entity.PatientLocationMapEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import gov.ca.emsa.pulse.common.domain.QueryLocationStatus;

public class PatientOrganizationMapDTO {
	private Long id;
	private Long patientId;
	private Long organizationId;
	private OrganizationDTO org;
	private String orgPatientRecordId;
	private QueryLocationStatus documentsQueryStatus;
	private Date documentsQueryStart;
	private Date documentsQueryEnd;
	private List<DocumentDTO> documents;
	
	public PatientOrganizationMapDTO() {
		documents = new ArrayList<DocumentDTO>();
	}
	
	public PatientOrganizationMapDTO(PatientLocationMapEntity entity) {
		this();
		this.id = entity.getId();
		this.patientId = entity.getPatientId();
		this.organizationId = entity.getLocationId();
		if(entity.getLocation() != null) {
			this.org = new OrganizationDTO(entity.getLocation());
		}
		this.orgPatientRecordId = entity.getExternalPatientRecordId();
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
	public Long getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}
	public String getOrgPatientRecordId() {
		return orgPatientRecordId;
	}
	public void setOrgPatientRecordId(String orgPatientRecordId) {
		this.orgPatientRecordId = orgPatientRecordId;
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

	public OrganizationDTO getOrg() {
		return org;
	}

	public void setOrg(OrganizationDTO org) {
		this.org = org;
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
