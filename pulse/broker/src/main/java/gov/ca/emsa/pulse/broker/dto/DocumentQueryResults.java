package gov.ca.emsa.pulse.broker.dto;

import java.util.ArrayList;
import java.util.List;

import gov.ca.emsa.pulse.cten.IheStatus;

public class DocumentQueryResults {
	private List<DocumentDTO> results;
	private IheStatus status;
	
	public DocumentQueryResults() {
		this.results = new ArrayList<DocumentDTO>();
	}

	public List<DocumentDTO> getResults() {
		return results;
	}

	public void setResults(List<DocumentDTO> results) {
		this.results = results;
	}

	public IheStatus getStatus() {
		return status;
	}

	public void setStatus(IheStatus status) {
		this.status = status;
	}
}
