package gov.ca.emsa.pulse.common.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PatientLocationMap {
	private Long id;
	private Long patientId;
	private Location location;
	private QueryLocationStatus documentsQueryStatus;
	private Date documentsQueryStart;
	private Date documentsQueryEnd;
	private List<Document> documents;
	
	public PatientLocationMap() {
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

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public QueryLocationStatus getDocumentsQueryStatus() {
		return documentsQueryStatus;
	}

	public void setDocumentsQueryStatus(QueryLocationStatus documentsQueryStatus) {
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
