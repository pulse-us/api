package gov.ca.emsa.pulse.broker.dto;

import gov.ca.emsa.pulse.broker.entity.DocumentEntity;
import gov.ca.emsa.pulse.broker.entity.PatientOrganizationMapEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PatientOrganizationMapDTO {
	private Long id;
	private Long patientId;
	private Long organizationId;
	private OrganizationDTO org;
	private String orgPatientId;
	private String documentsQueryStatus;
	private Boolean documentsQuerySuccess;
	private Date documentsQueryStart;
	private Date documentsQueryEnd;
	private List<DocumentDTO> documents;
	
	public PatientOrganizationMapDTO() {
		documents = new ArrayList<DocumentDTO>();
	}
	
	public PatientOrganizationMapDTO(PatientOrganizationMapEntity entity) {
		this();
		this.id = entity.getId();
		this.patientId = entity.getPatientId();
		this.organizationId = entity.getOrganizationId();
		if(entity.getOrganization() != null) {
			this.org = new OrganizationDTO(entity.getOrganization());
		}
		this.orgPatientId = entity.getOrganizationPatientId();
		this.documentsQueryStatus = entity.getDocumentsQueryStatus();
		this.documentsQuerySuccess = entity.getDocumentsQuerySuccess();
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
	public void setPatientId(Long patientId) {
		this.patientId = patientId;
	}
	public Long getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}
	public String getOrgPatientId() {
		return orgPatientId;
	}
	public void setOrgPatientId(String orgPatientId) {
		this.orgPatientId = orgPatientId;
	}
	public String getDocumentsQueryStatus() {
		return documentsQueryStatus;
	}
	public void setDocumentsQueryStatus(String documentsQueryStatus) {
		this.documentsQueryStatus = documentsQueryStatus;
	}
	public Boolean getDocumentsQuerySuccess() {
		return documentsQuerySuccess;
	}
	public void setDocumentsQuerySuccess(Boolean documentsQuerySuccess) {
		this.documentsQuerySuccess = documentsQuerySuccess;
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
}
