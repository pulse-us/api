package gov.ca.emsa.pulse.broker.dao;

import gov.ca.emsa.pulse.broker.domain.EndpointTypeEnum;
import gov.ca.emsa.pulse.broker.dto.EndpointDTO;
import gov.ca.emsa.pulse.broker.entity.EndpointEntity;

import java.util.List;

public interface EndpointDAO {
	
	public EndpointDTO create(EndpointDTO endpoint);
	public EndpointDTO update(EndpointDTO endpoint);
	public List<EndpointEntity> getAllEntities();
	public List<EndpointDTO> findAll();
	public List<EndpointDTO> findAllOfType(List<EndpointTypeEnum> types);
	public void delete(EndpointDTO endpointDto);
	public EndpointDTO findById(Long id);
	public EndpointDTO findByExternalId(String externalId);
	public EndpointDTO findByLocationIdAndType(Long locationId, EndpointTypeEnum type);
}
