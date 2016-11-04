package gov.ca.emsa.pulse.broker.dao.impl;

import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import gov.ca.emsa.pulse.broker.dao.PatientDiscoveryQueryStatisticsDAO;
import gov.ca.emsa.pulse.broker.entity.PatientDiscoveryRequestStatisticsEntity;

@Repository
public class PatientDiscoveryQueryStatisticsDAOImpl extends BaseDAOImpl  
	implements PatientDiscoveryQueryStatisticsDAO {
	private static final Logger logger = LogManager.getLogger(PatientDiscoveryQueryStatisticsDAOImpl.class);

	public List<PatientDiscoveryRequestStatisticsEntity> getStatistics(Date startFilter, Date endFilter) {
		logger.info("Getting statistics between " + startFilter + " and " + endFilter);
		String sql = 
		"SELECT " +
			"organization.id as organization_id, " +
			"organization.name as organization_name, " +
			"COALESCE(allRequests.totalCount, 0) as total_request_count, " + 
			"EXTRACT(EPOCH from allRequests.averageTime) as total_request_average_seconds, " +
			"COALESCE(successfulRequests.totalCount, 0) as successful_request_count, " +
			"EXTRACT(EPOCH from successfulRequests.averageTime) as successful_request_average_seconds, " +
			"COALESCE(failedRequests.totalCount, 0) as failed_request_count, " +
			"EXTRACT(EPOCH from failedRequests.averageTime) as failed_request_average_seconds, " +
			"COALESCE(cancelledRequests.totalCount, 0) as cancelled_request_count, " +
			"EXTRACT(EPOCH from cancelledRequests.averageTime) as cancelled_request_average_seconds " +
			"FROM organization " +
			"LEFT OUTER JOIN  " +
				"(SELECT count(*) as totalCount,  " + 
				"	AVG(end_date - start_date) as averageTime, organization_id " + 
				"	FROM pulse.patient_discovery_query_stats " +
				"	WHERE status NOT LIKE 'Active' ";
		if(startFilter != null) {
			sql += "AND start_date >= :startFilter ";
		}
		if(endFilter != null) {
			sql += "AND end_date <= :endFilter ";
		}
		sql +=	"	group by organization_id) allRequests " +
				"ON organization.id = allRequests.organization_id " +
			"LEFT OUTER JOIN " +
				"(SELECT count(*) as totalCount, " + 
					"AVG(end_date - start_date) as averageTime, organization_id " + 
					"FROM pulse.patient_discovery_query_stats " +
					"WHERE status LIKE 'Successful' ";
		if(startFilter != null) {
			sql += "AND start_date >= :startFilter ";
		}
		if(endFilter != null) {
			sql += "AND end_date <= :endFilter ";
		}
		sql += "group by organization_id, status) successfulRequests " +
				"ON organization.id = successfulRequests.organization_id " +
			"LEFT OUTER JOIN " +
				"(SELECT count(*) as totalCount, AVG(end_date - start_date) as averageTime, " + 
					"organization_id  " +
					"FROM pulse.patient_discovery_query_stats " +
					"WHERE status LIKE 'Failed' ";
		if(startFilter != null) {
			sql += "AND start_date >= :startFilter ";
		}
		if(endFilter != null) {
			sql += "AND end_date <= :endFilter ";
		}
		sql += "group by organization_id, status) failedRequests " +
				"ON organization.id = failedRequests.organization_id " +
			"LEFT OUTER JOIN " +
				"(SELECT count(*) as totalCount, AVG(end_date - start_date) as averageTime, " +
					"organization_id  " +
					"FROM pulse.patient_discovery_query_stats " +
					"WHERE status LIKE 'Cancelled' ";
		if(startFilter != null) {
			sql += "AND start_date >= :startFilter ";
		}
		if(endFilter != null) {
			sql += "AND end_date <= :endFilter ";
		}
		sql += "group by organization_id, status) cancelledRequests " +
				"ON organization.id = cancelledRequests.organization_id";
		
		Query query = entityManager.createNativeQuery(sql, PatientDiscoveryRequestStatisticsEntity.class);
		
		if(startFilter != null) {
			query.setParameter("startFilter", startFilter);
		}
		if(endFilter != null) {
			query.setParameter("endFilter", endFilter);
		}
		
		List<PatientDiscoveryRequestStatisticsEntity> results = query.getResultList();
		return results;
	}
}