package gov.ca.emsa.pulse.broker.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import gov.ca.emsa.pulse.broker.dto.DocumentDTO;
import gov.ca.emsa.pulse.broker.dto.PatientOrganizationMapDTO;

public class PatientOrganizationMap {
	private Long id;
	private Long patientId;
	private Organization organization;
	private String documentsQueryStatus;
	private Boolean documentsQuerySuccess;
	private Date documentsQueryStart;
	private Date documentsQueryEnd;
	private List<Document> documents;
	
	public PatientOrganizationMap() {
		this.documents = new ArrayList<Document>();
	}
	
	public PatientOrganizationMap(PatientOrganizationMapDTO dto) {
		this();
		this.id = dto.getId();
		this.patientId = dto.getPatientId();
		this.organization = new Organization(dto.getOrg());
		this.documentsQueryStatus = dto.getDocumentsQueryStatus();
		this.documentsQuerySuccess = dto.getDocumentsQuerySuccess();
		this.documentsQueryStart = dto.getDocumentsQueryStart();
		this.documentsQueryEnd = dto.getDocumentsQueryEnd();
		
		if(dto.getDocuments() != null && dto.getDocuments().size() > 0) { 
			for(DocumentDTO docDto : dto.getDocuments()) {
				Document doc = new Document(docDto);
				this.documents.add(doc);
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

	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
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

	public List<Document> getDocuments() {
		return documents;
	}

	public void setDocuments(List<Document> documents) {
		this.documents = documents;
	}
}
