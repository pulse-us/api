package gov.ca.emsa.pulse.broker.dao.impl;

import java.util.List;

import javax.persistence.Query;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import gov.ca.emsa.pulse.broker.dao.QueryStatusDAO;
import gov.ca.emsa.pulse.broker.entity.QueryLocationStatusEntity;
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
	
	public QueryLocationStatusEntity getQueryLocationStatusByName(String name) {
		Query statusQuery = entityManager.createQuery("from QueryLocationStatusEntity "
				+ "WHERE status = '" + name + "'",
				QueryLocationStatusEntity.class);
		List<QueryLocationStatusEntity> results = statusQuery.getResultList();
		
		QueryLocationStatusEntity result = null;
		if(results == null || results.size() == 0) {
			logger.error("Could not find queryLocationStatus '" + name + "'");
		} else {
			result = results.get(0);
		}
		return result;
	}
}
