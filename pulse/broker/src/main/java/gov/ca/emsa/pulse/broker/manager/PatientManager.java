package gov.ca.emsa.pulse.broker.manager;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;

import gov.ca.emsa.pulse.broker.domain.User;
import gov.ca.emsa.pulse.broker.dto.PatientDTO;
import gov.ca.emsa.pulse.broker.dto.PatientQueryResultDTO;
import gov.ca.emsa.pulse.broker.dto.QueryDTO;

public interface PatientManager {
	public PatientDTO getPatientById(Long patientId);
	public List<PatientDTO> searchPatients(PatientDTO toSearch);
	public QueryDTO queryPatients(User user, String samlMessage, PatientDTO searchParams) throws JsonProcessingException;
	public PatientDTO create(PatientDTO toCreate);
	public PatientQueryResultDTO mapPatientToQuery(PatientQueryResultDTO toCreate);
	public PatientDTO update(PatientDTO toUpdate);
	public void cleanupPatientCache(Date oldestAllowedPatient);
	public List<PatientDTO> getPatientsByQuery(Long queryId);
}
