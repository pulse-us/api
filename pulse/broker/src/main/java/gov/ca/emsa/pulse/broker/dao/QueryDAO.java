package gov.ca.emsa.pulse.broker.dao;

import gov.ca.emsa.pulse.broker.dto.QueryDTO;
import gov.ca.emsa.pulse.broker.dto.QueryLocationMapDTO;

import java.util.Date;
import java.util.List;

public interface QueryDAO {
	public QueryDTO create(QueryDTO dto);
	public QueryLocationMapDTO updateQueryOrganization(QueryLocationMapDTO orgStatus);
	public QueryDTO update(QueryDTO dto);
	public QueryLocationMapDTO createQueryOrganization(QueryLocationMapDTO orgStatus);
	public void delete(Long id);
	public List<QueryDTO> findAllForUser(String userToken);	
	public List<QueryDTO> findAllForUserWithStatus(String userToken, String status);	
	public QueryDTO getById(Long id);
	public QueryLocationMapDTO getQueryOrganizationById(Long queryOrgId);
	public QueryLocationMapDTO getQueryOrganizationByQueryAndOrg(Long queryId, Long orgId);
	public void deleteItemsOlderThan(Date oldestDate);
	public Boolean hasActiveOrganizations(Long queryId);
}