package gov.ca.emsa.pulse.broker.dao;

import java.util.List;

import gov.ca.emsa.pulse.broker.dto.PatientDTO;

public interface PatientDAO {
	public PatientDTO create(PatientDTO dto);
	public PatientDTO update(PatientDTO dto);
	public void delete(Long id);
	public List<PatientDTO> findAll();	
	public PatientDTO getById(Long id);
	public List<PatientDTO> getByValues(PatientDTO dto);
}
