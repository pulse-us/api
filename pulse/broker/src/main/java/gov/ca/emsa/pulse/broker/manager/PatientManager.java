package gov.ca.emsa.pulse.broker.manager;

import java.util.Date;
import java.util.List;

import gov.ca.emsa.pulse.broker.dto.PatientDTO;

public interface PatientManager {
	public PatientDTO getPatientById(Long patientId);
	public List<PatientDTO> getPatientsAtAcf(Long acfId);
	public PatientDTO create(PatientDTO toCreate);
	public PatientDTO update(PatientDTO toUpdate);
	public void cleanupPatientCache(Date oldestAllowedPatient);
}
