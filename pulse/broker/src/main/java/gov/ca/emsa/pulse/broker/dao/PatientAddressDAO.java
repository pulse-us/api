package gov.ca.emsa.pulse.broker.dao;

import gov.ca.emsa.pulse.broker.dto.PatientAddressDTO;

import java.util.List;

public interface PatientAddressDAO {
	public PatientAddressDTO create(PatientAddressDTO dto);
	public PatientAddressDTO update(PatientAddressDTO dto);
	public void delete(Long id);
	public List<PatientAddressDTO> findAll();	
	public PatientAddressDTO getById(Long id);
	public PatientAddressDTO getByValues(PatientAddressDTO address);
}
