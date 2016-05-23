package gov.ca.emsa.pulse.broker.manager;

import java.util.Date;
import java.util.List;

import gov.ca.emsa.pulse.broker.dto.PatientDTO;

public interface PatientManager {
	public List<PatientDTO> queryPatients(String firstName, String lastName);
	public void cleanupPatientCache(Date oldestAllowedPatient);
}
