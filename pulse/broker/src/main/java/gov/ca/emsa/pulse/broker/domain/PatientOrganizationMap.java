package gov.ca.emsa.pulse.broker.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PatientOrganizationMap {
	private String id;
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
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
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
