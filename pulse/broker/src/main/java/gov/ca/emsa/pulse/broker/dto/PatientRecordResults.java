package gov.ca.emsa.pulse.broker.dto;

import java.util.ArrayList;
import java.util.List;

import gov.ca.emsa.pulse.cten.IheStatus;

public class PatientRecordResults {
	private List<PatientRecordDTO> results;
	private IheStatus status;
	
	public PatientRecordResults() {
		this.results = new ArrayList<PatientRecordDTO>();
	}

	public List<PatientRecordDTO> getResults() {
		return results;
	}

	public void setResults(List<PatientRecordDTO> results) {
		this.results = results;
	}

	public IheStatus getStatus() {
		return status;
	}

	public void setStatus(IheStatus status) {
		this.status = status;
	}
}
