package gov.ca.emsa.pulse.broker.dao;

import gov.ca.emsa.pulse.broker.dto.PatientNameDTO;

public interface PatientNameDAO {
	public PatientNameDTO create(PatientNameDTO dto);
	public PatientNameDTO update(PatientNameDTO dto);
	
	public void delete(Long id);
	public PatientNameDTO getById(Long id);

}
