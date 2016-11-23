package gov.ca.emsa.pulse.broker.dao;

import gov.ca.emsa.pulse.broker.dto.PatientDTO;
import gov.ca.emsa.pulse.broker.dto.PatientLocationMapDTO;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public interface PatientDAO {
	public PatientDTO create(PatientDTO dto) throws SQLException;
	public PatientDTO update(PatientDTO dto) throws SQLException;
	
	public PatientLocationMapDTO createPatientLocationMap(PatientLocationMapDTO toCreate) throws SQLException;
	public PatientLocationMapDTO updatePatientLocationMap(PatientLocationMapDTO toUpdate) throws SQLException;
	
	public void delete(Long id) throws SQLException;
	public PatientDTO getById(Long id);
	public PatientLocationMapDTO getPatientLocationMapById(Long id);

	public List<PatientDTO> getPatientsAtAcf(Long acfId);
	public void deleteItemsOlderThan(Date oldestItem) throws SQLException;
}
