package gov.ca.emsa.pulse.broker.manager;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;

import gov.ca.emsa.pulse.broker.dto.PatientDTO;
import gov.ca.emsa.pulse.broker.dto.QueryDTO;

public interface PatientManager {
	public PatientDTO getPatientById(Long patientId);
	public QueryDTO queryPatients(String samlMessage, String firstName, String lastName) throws JsonProcessingException;
	public void cleanupPatientCache(Date oldestAllowedPatient);
	public List<PatientDTO> getPatientsByQuery(Long queryId);
}
