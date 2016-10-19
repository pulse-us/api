package gov.ca.emsa.pulse.broker.dao;

import gov.ca.emsa.pulse.broker.dto.PatientAddressLineDTO;

import java.util.List;

public interface PatientAddressLineDAO {
	public PatientAddressLineDTO create(PatientAddressLineDTO dto);
	public PatientAddressLineDTO update(PatientAddressLineDTO dto);
	public void delete(Long id);
	public List<PatientAddressLineDTO> getByPatientAddressId(Long id);
	public PatientAddressLineDTO getById(Long id);
	public void deleteAllLines(Long patientAddressId);
}
