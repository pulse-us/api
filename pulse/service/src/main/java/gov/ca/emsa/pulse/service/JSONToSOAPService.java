package gov.ca.emsa.pulse.service;

import gov.ca.emsa.pulse.common.domain.PatientRecord;

import java.util.List;

import org.hl7.v3.PRPAIN201310UV02;

public interface JSONToSOAPService {
	public PRPAIN201310UV02 convertPatientRecordListToSOAPResponse(List<PatientRecord> patientRecords);
}
