package gov.ca.emsa.pulse.broker.dao;

import gov.ca.emsa.pulse.broker.dto.AlternateCareFacilityDTO;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityExistsException;

public interface AlternateCareFacilityDAO {
	public AlternateCareFacilityDTO create(AlternateCareFacilityDTO dto) throws EntityExistsException;
	public AlternateCareFacilityDTO update(AlternateCareFacilityDTO dto);
	public void delete(Long id);
	public List<AlternateCareFacilityDTO> findAll();	
	public AlternateCareFacilityDTO getById(Long id);
	public List<AlternateCareFacilityDTO> getByName(String name);
	public void deleteItemsOlderThan(Date oldestItem);
}
