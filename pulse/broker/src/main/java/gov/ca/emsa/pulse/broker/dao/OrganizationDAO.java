package gov.ca.emsa.pulse.broker.dao;

import gov.ca.emsa.pulse.broker.dto.LocationDTO;
import gov.ca.emsa.pulse.broker.entity.LocationEntity;

import java.util.List;

public interface OrganizationDAO {
	
	public LocationDTO create(LocationDTO org);
	public LocationDTO update(LocationDTO org);
	public List<LocationEntity> getAllEntities();
	public List<LocationDTO> findAll();
	public void delete(LocationDTO organizationDTO);
	public LocationDTO findById(Long id);
	public List<LocationDTO> findByName(String name);
}
