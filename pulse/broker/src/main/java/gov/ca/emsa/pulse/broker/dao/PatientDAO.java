package gov.ca.emsa.pulse.broker.dao;

import java.util.Date;
import java.util.List;

import gov.ca.emsa.pulse.broker.dto.PatientDTO;
import gov.ca.emsa.pulse.broker.dto.PatientOrganizationMapDTO;

public interface PatientDAO {
	public PatientDTO create(PatientDTO dto);
	public PatientDTO update(PatientDTO dto);
	
	public PatientOrganizationMapDTO createOrgMap(PatientOrganizationMapDTO toCreate);
	public PatientOrganizationMapDTO updateOrgMap(PatientOrganizationMapDTO toUpdate);
	
	public void delete(Long id);
	public PatientDTO getById(Long id);
	public PatientOrganizationMapDTO getPatientOrgMapById(Long id);

	public List<PatientDTO> getPatientsAtAcf(Long acfId);
	public void deleteItemsOlderThan(Date oldestItem);
}
