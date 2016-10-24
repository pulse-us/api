package gov.ca.emsa.pulse.broker.dao;

import gov.ca.emsa.pulse.broker.dto.NameTypeDTO;
import gov.ca.emsa.pulse.broker.dto.PatientGenderDTO;

public interface PatientGenderDAO {
	public PatientGenderDTO create(PatientGenderDTO dto);
	public PatientGenderDTO update(PatientGenderDTO dto);
	
	public void delete(Long id);
	public PatientGenderDTO getById(Long id);
}
