package gov.ca.emsa.pulse.broker.dao;

import gov.ca.emsa.pulse.broker.domain.EndpointTypeEnum;
import gov.ca.emsa.pulse.broker.dto.LocationDTO;
import gov.ca.emsa.pulse.broker.entity.LocationEntity;

import java.util.List;

public interface LocationDAO {
	
	public LocationDTO create(LocationDTO location);
	public LocationDTO update(LocationDTO location);
	public List<LocationEntity> getAllEntities();
	public List<LocationDTO> findAll();
	public List<LocationDTO> findAllWithEndpointType(List<EndpointTypeEnum> types);
	public void delete(LocationDTO locationDto);
	public LocationDTO findById(Long id);
	public LocationDTO findByExternalId(String externalId);
	public List<LocationDTO> findByName(String name);
}
