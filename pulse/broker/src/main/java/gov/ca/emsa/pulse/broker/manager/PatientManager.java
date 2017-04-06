package gov.ca.emsa.pulse.broker.manager;

import gov.ca.emsa.pulse.auth.user.CommonUser;
import gov.ca.emsa.pulse.broker.dto.PatientDTO;
import gov.ca.emsa.pulse.broker.dto.PatientEndpointMapDTO;

import java.sql.SQLException;
import java.util.List;

public interface PatientManager extends CachedDataManager {
	public PatientDTO getPatientById(Long patientId);
	public List<PatientDTO> getPatientsAtAcf(Long acfId);
	public List<PatientEndpointMapDTO> getPatientEndpointMaps(Long patientId, Long endpointId);
	public PatientDTO create(PatientDTO toCreate) throws SQLException;
	public PatientDTO update(PatientDTO toUpdate) throws SQLException;
	public void delete(Long patientId) throws SQLException;
	
	public PatientEndpointMapDTO updatePatientEndpointMap(PatientEndpointMapDTO toUpdate) throws SQLException;
	public PatientEndpointMapDTO createEndpointMapForDocumentDiscovery(PatientDTO patient, Long patientRecordId) throws SQLException;
	public PatientDTO cancelQueryForDocuments(Long patientId, Long endpointId) throws SQLException;
	public PatientDTO requeryForDocuments(Long patientId, Long endpointId, CommonUser user) throws SQLException;
}
