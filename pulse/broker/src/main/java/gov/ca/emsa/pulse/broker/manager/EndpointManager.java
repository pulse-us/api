package gov.ca.emsa.pulse.broker.manager;

import gov.ca.emsa.pulse.broker.domain.EndpointStatusEnum;
import gov.ca.emsa.pulse.broker.domain.EndpointTypeEnum;
import gov.ca.emsa.pulse.broker.dto.EndpointDTO;
import gov.ca.emsa.pulse.broker.dto.LocationEndpointMapDTO;
import gov.ca.emsa.pulse.common.domain.Endpoint;

import java.util.List;

public interface EndpointManager {
	public EndpointDTO getById(Long id);
	public EndpointDTO getByExternalId(String id);
	public List<EndpointDTO> getByType(List<EndpointTypeEnum> types);
	public List<EndpointDTO> getByStatusAndType(List<EndpointStatusEnum> statuses, List<EndpointTypeEnum> types);
	public void updateEndpoints(List<Endpoint> endpoints);
	public List<EndpointDTO> getAll();
	public void updateEndpointLocationMappings(List<LocationEndpointMapDTO> mappings);
}
