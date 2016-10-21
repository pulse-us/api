package gov.ca.emsa.pulse.broker.dao;

import java.util.Date;
import java.util.List;

import gov.ca.emsa.pulse.broker.dto.QueryDTO;
import gov.ca.emsa.pulse.broker.dto.QueryOrganizationDTO;

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
	public QueryOrganizationDTO getQueryOrganizationByQueryAndOrg(Long queryId, Long orgId);
	public void deleteItemsOlderThan(Date oldestDate);
	public Boolean hasActiveOrganizations(Long queryId);
}