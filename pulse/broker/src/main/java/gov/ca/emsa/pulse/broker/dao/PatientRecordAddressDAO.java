package gov.ca.emsa.pulse.broker.dao;

import gov.ca.emsa.pulse.broker.dto.PatientRecordAddressDTO;

import java.util.List;

public interface PatientRecordAddressDAO {
	public PatientRecordAddressDTO create(PatientRecordAddressDTO dto);
	public PatientRecordAddressDTO update(PatientRecordAddressDTO dto);
	public void delete(Long id);
	public List<PatientRecordAddressDTO> findAll();	
	public PatientRecordAddressDTO getById(Long id);
	public PatientRecordAddressDTO getByValues(PatientRecordAddressDTO address);
}
