package gov.ca.emsa.pulse.broker.dao.impl;

import java.util.List;

import javax.persistence.Query;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import gov.ca.emsa.pulse.broker.dao.QueryStatusDAO;
import gov.ca.emsa.pulse.broker.entity.QueryEndpointStatusEntity;
import gov.ca.emsa.pulse.broker.entity.QueryStatusEntity;

@Repository
public class QueryStatusDAOImpl extends BaseDAOImpl implements QueryStatusDAO {
	private static final Logger logger = LogManager.getLogger(QueryStatusDAOImpl.class);

	public QueryStatusEntity getQueryStatusByName(String name) {
		Query statusQuery = entityManager.createQuery("from QueryStatusEntity "
				+ "WHERE status = '" + name + "'",
				QueryStatusEntity.class);
		List<QueryStatusEntity> results = statusQuery.getResultList();
		
		QueryStatusEntity result = null;
		if(results == null || results.size() == 0) {
			logger.error("Could not find queryStatus '" + name + "'");
		} else {
			result = results.get(0);
		}
		return result;
	}
	
	public QueryEndpointStatusEntity getQueryEndpointStatusByName(String name) {
		Query statusQuery = entityManager.createQuery("from QueryEndpointStatusEntity "
				+ "WHERE status = '" + name + "'",
				QueryEndpointStatusEntity.class);
		List<QueryEndpointStatusEntity> results = statusQuery.getResultList();
		
		QueryEndpointStatusEntity result = null;
		if(results == null || results.size() == 0) {
			logger.error("Could not find QueryEndpointStatus '" + name + "'");
		} else {
			result = results.get(0);
		}
		return result;
	}
}
