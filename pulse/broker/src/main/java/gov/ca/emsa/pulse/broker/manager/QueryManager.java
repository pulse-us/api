package gov.ca.emsa.pulse.broker.manager;

import java.util.List;

import gov.ca.emsa.pulse.broker.dto.QueryDTO;

public interface QueryManager {
	public List<QueryDTO> getAllQueriesForUser(String userKey);
	public List<QueryDTO> getActiveQueriesForUser(String userKey);
	public String getQueryStatus(Long queryId);
	public QueryDTO getQueryStatusDetails(Long queryId);
	public QueryDTO updateQuery(QueryDTO toUpdate);
	public QueryDTO createQuery(QueryDTO toCreate);
}