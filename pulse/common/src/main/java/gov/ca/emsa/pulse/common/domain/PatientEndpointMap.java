package gov.ca.emsa.pulse.common.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PatientEndpointMap {
	private Long id;
	private Long patientId;
	private Location location;
	private String homeCommunityId;
	private String externalPatientRecordId; // PID + AA
	private Endpoint endpoint;
	private QueryEndpointStatus documentsQueryStatus;
	private Date documentsQueryStart;
	private Date documentsQueryEnd;
	private List<Document> documents;
	
	public PatientEndpointMap() {
		this.documents = new ArrayList<Document>();
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

	public Endpoint getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(Endpoint endpoint) {
		this.endpoint = endpoint;
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


	public QueryEndpointStatus getDocumentsQueryStatus() {
		return documentsQueryStatus;
	}

	public void setDocumentsQueryStatus(QueryEndpointStatus documentsQueryStatus) {
		this.documentsQueryStatus = documentsQueryStatus;
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
