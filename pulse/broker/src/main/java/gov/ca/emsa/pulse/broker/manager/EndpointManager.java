package gov.ca.emsa.pulse.broker.manager;

import gov.ca.emsa.pulse.broker.domain.EndpointTypeEnum;
import gov.ca.emsa.pulse.broker.dto.EndpointDTO;
import gov.ca.emsa.pulse.common.domain.Endpoint;

import java.util.List;

public interface EndpointManager {
	public EndpointDTO getById(Long id);
	public List<EndpointDTO> getByType(List<EndpointTypeEnum> types);
	public void updateEndpoints(List<Endpoint> endpoints);
	public List<EndpointDTO> getAll();
}
