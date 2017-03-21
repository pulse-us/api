package gov.ca.emsa.pulse.broker.dao;

import gov.ca.emsa.pulse.broker.entity.QueryEndpointStatusEntity;
import gov.ca.emsa.pulse.broker.entity.QueryStatusEntity;

public interface QueryStatusDAO {
	public QueryStatusEntity getQueryStatusByName(String name);
	public QueryEndpointStatusEntity getQueryEndpointStatusByName(String name);
}
