package gov.ca.emsa.pulse.broker.dao;

import gov.ca.emsa.pulse.broker.dto.EndpointDTO;
import gov.ca.emsa.pulse.broker.dto.LocationDTO;
import gov.ca.emsa.pulse.broker.dto.LocationEndpointMapDTO;

import java.util.List;

public interface LocationEndpointMapDAO {
	
	public List<LocationEndpointMapDTO> findAll();
	public LocationEndpointMapDTO create(Long locationId, Long endpointId);
	public boolean exists(Long locationId, Long endpointId);
	public List<LocationDTO> findLocationsForEndpoint(Long endpointId);
	public List<EndpointDTO> findEndpointsForLocation(Long locationId);
	public void delete(Long locationId, Long endpointId);
	public void delete(Long id);
}
