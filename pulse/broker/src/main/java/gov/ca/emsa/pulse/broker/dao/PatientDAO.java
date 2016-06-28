package gov.ca.emsa.pulse.broker.dao;

import java.util.Date;
import java.util.List;

import gov.ca.emsa.pulse.broker.dto.PatientDTO;

public interface PatientDAO {
	public PatientDTO create(PatientDTO dto);
	public PatientDTO update(PatientDTO dto);
	public void delete(Long id);
	public PatientDTO getById(Long id);
	public List<PatientDTO> getPatientsAtAcf(Long acfId);
	public void deleteItemsOlderThan(Date oldestItem);
}
