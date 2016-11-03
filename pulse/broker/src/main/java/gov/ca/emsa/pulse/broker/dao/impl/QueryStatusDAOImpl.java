package gov.ca.emsa.pulse.broker.dao.impl;

import java.util.List;

import javax.persistence.Query;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import gov.ca.emsa.pulse.broker.dao.QueryStatusDAO;
import gov.ca.emsa.pulse.broker.entity.QueryOrganizationStatusEntity;

@Repository
public class QueryStatusDAOImpl extends BaseDAOImpl implements QueryStatusDAO {
	private static final Logger logger = LogManager.getLogger(QueryStatusDAOImpl.class);

	public QueryOrganizationStatusEntity getStatusByName(String name) {
		Query statusQuery = entityManager.createQuery("from QueryOrganizationStatusEntity "
				+ "WHERE status = '" + name + "'",
				QueryOrganizationStatusEntity.class);
		List<QueryOrganizationStatusEntity> results = statusQuery.getResultList();
		
		QueryOrganizationStatusEntity result = null;
		if(results == null || results.size() == 0) {
			logger.error("Could not find queryOrgStatus '" + name + "'");
		} else {
			result = results.get(0);
		}
		return result;
	}
}
