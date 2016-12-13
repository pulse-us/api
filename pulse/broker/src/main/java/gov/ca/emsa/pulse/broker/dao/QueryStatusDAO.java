package gov.ca.emsa.pulse.broker.dao;

import gov.ca.emsa.pulse.broker.entity.QueryLocationStatusEntity;
import gov.ca.emsa.pulse.broker.entity.QueryStatusEntity;

public interface QueryStatusDAO {
	public QueryStatusEntity getQueryStatusByName(String name);
	public QueryLocationStatusEntity getQueryLocationStatusByName(String name);
}
