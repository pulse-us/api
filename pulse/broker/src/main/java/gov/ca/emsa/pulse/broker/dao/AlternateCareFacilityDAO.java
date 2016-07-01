package gov.ca.emsa.pulse.broker.dao;

import java.util.Date;
import java.util.List;

import gov.ca.emsa.pulse.broker.dto.AlternateCareFacilityDTO;

public interface AlternateCareFacilityDAO {
	public AlternateCareFacilityDTO create(AlternateCareFacilityDTO dto);
	public AlternateCareFacilityDTO update(AlternateCareFacilityDTO dto);
	public void delete(Long id);
	public List<AlternateCareFacilityDTO> findAll();	
	public AlternateCareFacilityDTO getById(Long id);
	public List<AlternateCareFacilityDTO> getByName(String name);
	public void deleteItemsOlderThan(Date oldestItem);
}
