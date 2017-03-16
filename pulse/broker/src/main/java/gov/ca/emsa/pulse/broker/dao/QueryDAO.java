package gov.ca.emsa.pulse.broker.dao;

import gov.ca.emsa.pulse.broker.dto.QueryDTO;
import gov.ca.emsa.pulse.broker.dto.QueryEndpointMapDTO;
import gov.ca.emsa.pulse.common.domain.QueryStatus;

import java.util.Date;
import java.util.List;

public interface QueryDAO {
	public QueryDTO create(QueryDTO dto);
	public QueryEndpointMapDTO updateQueryEndpointMap(QueryEndpointMapDTO queryEndpointMap);
	public QueryDTO update(QueryDTO dto);
	public QueryEndpointMapDTO createQueryEndpointMap(QueryEndpointMapDTO queryEndpointMap);
	public void close(Long id);
	public List<QueryDTO> findAllForUser(String userToken);	
	public List<QueryDTO> findAllForUserWithStatus(String userToken, List<QueryStatus> status);	
	public QueryDTO getById(Long id);
	public QueryEndpointMapDTO getQueryEndpointById(Long id);
	public void deleteItemsOlderThan(Date oldestDate);
	public Boolean hasActiveLocations(Long queryId);
}