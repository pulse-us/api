package gov.ca.emsa.pulse.broker.dao;

import gov.ca.emsa.pulse.broker.dto.PatientDTO;
import gov.ca.emsa.pulse.broker.dto.PatientEndpointMapDTO;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public interface PatientDAO {
	public PatientDTO create(PatientDTO dto) throws SQLException;
	public PatientDTO update(PatientDTO dto) throws SQLException;
	
	public PatientEndpointMapDTO createPatientEndpointMap(PatientEndpointMapDTO toCreate) throws SQLException;
	public PatientEndpointMapDTO updatePatientEndpointMap(PatientEndpointMapDTO toUpdate) throws SQLException;
	
	public void delete(Long id) throws SQLException;
	public PatientDTO getById(Long id);
	public PatientEndpointMapDTO getPatientEndpointMapById(Long id);
	public List<PatientEndpointMapDTO> getPatientEndpointMaps(Long patientId, Long endpointId);
	
	public List<PatientDTO> getPatientsAtAcf(Long acfId);
	public void deleteItemsOlderThan(Date oldestItem) throws SQLException;
}
