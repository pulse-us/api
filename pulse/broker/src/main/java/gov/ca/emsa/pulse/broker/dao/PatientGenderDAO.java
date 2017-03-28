package gov.ca.emsa.pulse.broker.dao;

import gov.ca.emsa.pulse.broker.dto.NameTypeDTO;
import gov.ca.emsa.pulse.broker.dto.PatientGenderDTO;
import gov.ca.emsa.pulse.broker.entity.PatientGenderEntity;

public interface PatientGenderDAO {
	public PatientGenderDTO create(PatientGenderDTO dto);
	public PatientGenderDTO update(PatientGenderDTO dto);
	
	public void delete(Long id);
	public PatientGenderDTO getById(Long id);
	PatientGenderDTO getByCode(String code);
	public PatientGenderEntity getPubEntityByCode(String code);
}
