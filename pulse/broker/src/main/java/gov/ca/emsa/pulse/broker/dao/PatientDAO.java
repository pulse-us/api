package gov.ca.emsa.pulse.broker.dao;

import gov.ca.emsa.pulse.broker.dto.PatientDTO;
import gov.ca.emsa.pulse.broker.dto.PatientOrganizationMapDTO;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public interface PatientDAO {
	public PatientDTO create(PatientDTO dto) throws SQLException;
	public PatientDTO update(PatientDTO dto) throws SQLException;
	
	public PatientOrganizationMapDTO createOrgMap(PatientOrganizationMapDTO toCreate) throws SQLException;
	public PatientOrganizationMapDTO updateOrgMap(PatientOrganizationMapDTO toUpdate) throws SQLException;
	
	public void delete(Long id) throws SQLException;
	public PatientDTO getById(Long id);
	public PatientOrganizationMapDTO getPatientOrgMapById(Long id);

	public List<PatientDTO> getPatientsAtAcf(Long acfId);
	public void deleteItemsOlderThan(Date oldestItem) throws SQLException;
}
