package gov.ca.emsa.pulse.broker.dao;

import gov.ca.emsa.pulse.broker.entity.QueryLocationStatusEntity;

public interface QueryStatusDAO {
	public QueryLocationStatusEntity getStatusByName(String name);
}
