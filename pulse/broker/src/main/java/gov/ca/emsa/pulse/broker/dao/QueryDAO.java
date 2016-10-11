package gov.ca.emsa.pulse.broker.dao;

import gov.ca.emsa.pulse.broker.dto.QueryDTO;
import gov.ca.emsa.pulse.broker.dto.QueryOrganizationDTO;

import java.util.Date;
import java.util.List;

public interface QueryDAO {
	public QueryDTO create(QueryDTO dto);
	public QueryOrganizationDTO updateQueryOrganization(QueryOrganizationDTO orgStatus);
	public QueryDTO update(QueryDTO dto);
	public QueryOrganizationDTO createQueryOrganization(QueryOrganizationDTO orgStatus);
	public void delete(Long id);
	public List<QueryDTO> findAllForUser(String userToken);	
	public List<QueryDTO> findAllForUserWithStatus(String userToken, String status);	
	public QueryDTO getById(Long id);
	public QueryOrganizationDTO getQueryOrganizationById(Long queryOrgId);
	public void deleteItemsOlderThan(Date oldestDate);
	public Boolean hasActiveOrganizations(Long queryId);
}