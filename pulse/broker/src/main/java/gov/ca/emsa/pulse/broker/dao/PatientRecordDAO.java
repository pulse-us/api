package gov.ca.emsa.pulse.broker.dao;


import gov.ca.emsa.pulse.broker.dto.PatientRecordDTO;

public interface PatientRecordDAO {
	public PatientRecordDTO create(PatientRecordDTO dto);
	public PatientRecordDTO update(PatientRecordDTO dto);
	public void delete(Long id);
	public PatientRecordDTO getById(Long id);
}
