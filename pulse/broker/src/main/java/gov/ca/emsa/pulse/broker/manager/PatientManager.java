package gov.ca.emsa.pulse.broker.manager;

import java.util.List;

import gov.ca.emsa.pulse.broker.dto.PatientDTO;
import gov.ca.emsa.pulse.broker.dto.PatientOrganizationMapDTO;

public interface PatientManager extends CachedDataManager {
	public PatientDTO getPatientById(Long patientId);
	public List<PatientDTO> getPatientsAtAcf(Long acfId);
	public PatientDTO create(PatientDTO toCreate);
	public PatientDTO update(PatientDTO toUpdate);
	
	public PatientOrganizationMapDTO updateOrganizationMap(PatientOrganizationMapDTO toUpdate);
	public PatientOrganizationMapDTO createOrganizationMap(PatientOrganizationMapDTO toCreate);
	public PatientOrganizationMapDTO createOrganizationMapFromPatientRecord(PatientDTO patient, Long patientRecordId);	
}
