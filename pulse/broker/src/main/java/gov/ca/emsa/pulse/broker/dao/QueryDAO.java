package gov.ca.emsa.pulse.broker.dao;

import gov.ca.emsa.pulse.broker.dto.QueryDTO;
import gov.ca.emsa.pulse.broker.dto.QueryLocationMapDTO;

import java.util.Date;
import java.util.List;

public interface QueryDAO {
	public QueryDTO create(QueryDTO dto);
	public QueryLocationMapDTO updateQueryLocationMap(QueryLocationMapDTO queryLocationStatus);
	public QueryDTO update(QueryDTO dto);
	public QueryLocationMapDTO createQueryLocationMap(QueryLocationMapDTO queryLocationStatus);
	public void delete(Long id);
	public List<QueryDTO> findAllForUser(String userToken);	
	public List<QueryDTO> findAllForUserWithStatus(String userToken, String status);	
	public QueryDTO getById(Long id);
	public QueryLocationMapDTO getQueryLocationById(Long queryLocationMapId);
	public QueryLocationMapDTO getQueryLocationMapByQueryAndLocation(Long queryId, Long locationId);
	public void deleteItemsOlderThan(Date oldestDate);
	public Boolean hasActiveLocations(Long queryId);
}