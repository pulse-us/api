package gov.ca.emsa.pulse.broker.dao;

import gov.ca.emsa.pulse.broker.dto.PatientRecordNameDTO;

public interface PatientRecordNameDAO {
	public PatientRecordNameDTO create(PatientRecordNameDTO dto);
	public PatientRecordNameDTO update(PatientRecordNameDTO dto);
	
	public void delete(Long id);
	public PatientRecordNameDTO getById(Long id);

}
