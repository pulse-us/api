package gov.ca.emsa.pulse.broker.dao;

import java.util.List;

import gov.ca.emsa.pulse.broker.dto.QueryDTO;

public interface QueryDAO {
	public QueryDTO create(QueryDTO dto);
	public QueryDTO update(QueryDTO dto);
	public void delete(Long id);
	public List<QueryDTO> findAllForUser(String userToken);	
	public List<QueryDTO> findAllForUserWithStatus(String userToken, String status);	
	public QueryDTO getById(Long id);
}
