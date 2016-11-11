package gov.ca.emsa.pulse.broker.dao;

import gov.ca.emsa.pulse.broker.dto.PatientRecordAddressLineDTO;

import java.util.List;

public interface PatientRecordAddressLineDAO {
	public PatientRecordAddressLineDTO create(PatientRecordAddressLineDTO dto);
	public PatientRecordAddressLineDTO update(PatientRecordAddressLineDTO dto);
	public void delete(Long id);
	public List<PatientRecordAddressLineDTO> getByPatientAddressId(Long id);
	public PatientRecordAddressLineDTO getById(Long id);
	public void deleteAllLines(Long patientAddressId);
}
