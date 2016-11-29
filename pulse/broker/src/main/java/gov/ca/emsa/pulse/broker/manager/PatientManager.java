package gov.ca.emsa.pulse.broker.manager;

import gov.ca.emsa.pulse.broker.dto.PatientDTO;
import gov.ca.emsa.pulse.broker.dto.PatientLocationMapDTO;

import java.sql.SQLException;
import java.util.List;

public interface PatientManager extends CachedDataManager {
	public PatientDTO getPatientById(Long patientId);
	public List<PatientDTO> getPatientsAtAcf(Long acfId);
	public PatientDTO create(PatientDTO toCreate) throws SQLException;
	public PatientDTO update(PatientDTO toUpdate) throws SQLException;
	public void delete(Long patientId) throws SQLException;
	
	public PatientLocationMapDTO updatePatientLocationMap(PatientLocationMapDTO toUpdate) throws SQLException;
	public PatientLocationMapDTO createPatientLocationMap(PatientLocationMapDTO toCreate) throws SQLException;
	public PatientLocationMapDTO createPatientLocationMapFromPatientRecord(PatientDTO patient, Long patientRecordId) throws SQLException;	
}
