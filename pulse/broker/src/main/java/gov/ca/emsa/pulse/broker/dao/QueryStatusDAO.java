package gov.ca.emsa.pulse.broker.dao;

import gov.ca.emsa.pulse.broker.entity.QueryOrganizationStatusEntity;

public interface QueryStatusDAO {
	public QueryOrganizationStatusEntity getStatusByName(String name);
}
