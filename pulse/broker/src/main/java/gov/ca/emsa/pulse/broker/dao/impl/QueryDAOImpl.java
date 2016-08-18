package gov.ca.emsa.pulse.broker.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import gov.ca.emsa.pulse.broker.dao.QueryDAO;
import gov.ca.emsa.pulse.broker.dto.QueryDTO;
import gov.ca.emsa.pulse.broker.dto.QueryOrganizationDTO;
import gov.ca.emsa.pulse.broker.dto.QueryStatus;
import gov.ca.emsa.pulse.broker.entity.QueryEntity;
import gov.ca.emsa.pulse.broker.entity.QueryOrganizationEntity;

@Repository
public class QueryDAOImpl extends BaseDAOImpl implements QueryDAO {
	private static final Logger logger = LogManager.getLogger(QueryDAOImpl.class);
	
	@Override
	public QueryDTO create(QueryDTO dto) {
		
		QueryEntity query = new QueryEntity();
		query.setUserId(dto.getUserId());
		query.setStatus(QueryStatus.ACTIVE.name());
		query.setTerms(dto.getTerms());
		query.setLastReadDate(new Date());
		entityManager.persist(query);
		
		if(dto.getOrgStatuses() != null && dto.getOrgStatuses().size() > 0) {
			for(QueryOrganizationDTO orgStatus : dto.getOrgStatuses()) {
				QueryOrganizationEntity orgMap = new QueryOrganizationEntity();
				orgMap.setOrganizationId(orgStatus.getOrgId());
				orgMap.setQueryId(query.getId());
				orgMap.setStatus(QueryStatus.ACTIVE.name());
				orgMap.setStartDate(new Date());
				entityManager.persist(orgMap);
				query.getOrgStatuses().add(orgMap);
			}
		}
		
		entityManager.flush();
		return new QueryDTO(query);
	}

	public QueryOrganizationDTO getQueryOrganizationById(Long queryOrgId) {
		QueryOrganizationEntity entity = null;
		
		Query query = entityManager.createQuery( "SELECT q from QueryOrganizationEntity q "
				+ "where q.id = :entityid) ", 
				QueryOrganizationEntity.class );
		
		query.setParameter("entityid", queryOrgId);
		List<QueryOrganizationEntity> results = query.getResultList();
		if(results.size() != 0) {
			entity = results.get(0);
		}
		return new QueryOrganizationDTO(entity);
	}
	
	public QueryOrganizationDTO createQueryOrganization(QueryOrganizationDTO orgStatus) {
		QueryOrganizationEntity orgMap = new QueryOrganizationEntity();
		orgMap.setOrganizationId(orgStatus.getOrgId());
		orgMap.setQueryId(orgStatus.getQueryId());
		orgMap.setStatus(QueryStatus.ACTIVE.name());
		orgMap.setStartDate(new Date());
		entityManager.persist(orgMap);
		return new QueryOrganizationDTO(orgMap);
	}
	
	@Override
	public QueryOrganizationDTO updateQueryOrganization(QueryOrganizationDTO orgStatus) {
		logger.info("Update query organization " + orgStatus.getId() + " with status " + orgStatus.getStatus());
		QueryOrganizationEntity orgMap = this.getQueryStatusById(orgStatus.getId());
		if(orgStatus.getStatus().equalsIgnoreCase(QueryStatus.COMPLETE.name())) {
			orgMap.setEndDate(new Date());
			orgMap.setSuccess(orgStatus.getSuccess());
			orgMap.setStatus(QueryStatus.COMPLETE.name());
		} else {
			orgMap.setStatus(QueryStatus.ACTIVE.name());
		}
		orgMap = entityManager.merge(orgMap);
		entityManager.flush();
		logger.info("Updated orgStatus " + orgStatus.getId());
		return new QueryOrganizationDTO(orgMap);
	}
	
	@Override
	public QueryDTO update(QueryDTO dto) {
		QueryEntity query = this.getEntityById(dto.getId());
		logger.info("Set last read date for query " + dto.getId() + " to " + dto.getLastReadDate());
		query.setLastReadDate(dto.getLastReadDate());
		logger.info("Set status for query " + dto.getId() + " to " + dto.getStatus());
		query.setStatus(dto.getStatus());
		//terms and user wouldn't change
		//org statuses should be updated separately in a manager
		entityManager.merge(query);
		entityManager.flush();
		return new QueryDTO(query);
	}

	@Override
	public void delete(Long id) {
		try {
			QueryEntity toDelete = getEntityById(id);
			if(toDelete != null) {
				entityManager.remove(toDelete);
				entityManager.flush();
			}
		} catch(Exception ex) {
			logger.error("Could not delete query with id " + id + ". Maybe it was deleted by another thread?", ex);
		}
	}

	@Override
	public List<QueryDTO> findAllForUser(String user) {
		List<QueryEntity> result = this.getEntitiesByUser(user);
		List<QueryDTO> dtos = new ArrayList<QueryDTO>(result.size());
		for(QueryEntity entity : result) {
			dtos.add(new QueryDTO(entity));
		}
		return dtos;
	}
	
	@Override
	public List<QueryDTO> findAllForUserWithStatus(String userToken, String status) {
		List<QueryEntity> result = this.getEntitiesByUserAndStatus(userToken, status);
		List<QueryDTO> dtos = new ArrayList<QueryDTO>(result.size());
		for(QueryEntity entity : result) {
			dtos.add(new QueryDTO(entity));
		}
		return dtos;
	}
	
	@Override
	public QueryDTO getById(Long id) {
		
		QueryDTO dto = null;
		QueryEntity qe = this.getEntityById(id);
		
		if (qe != null) {
			dto = new QueryDTO(qe);
		}
		return dto;
	}
	
	@Override
	public void deleteItemsOlderThan(Date oldestDate) {			
		Query query = entityManager.createQuery( "from QueryEntity qe "
				+ " WHERE qe.lastReadDate <= :cacheDate");
		
		query.setParameter("cacheDate", oldestDate);
		List<QueryEntity> oldQueries = query.getResultList();
		
		if(oldQueries.size() > 0) {
			for(QueryEntity oldQuery : oldQueries) {
				delete(oldQuery.getId());
				logger.info("Deleted query with id " + oldQuery.getId() +" from the cache.");
			}
		} else {
			logger.info("Deleted 0 queries from the cache.");
		}
	}
	
	private List<QueryEntity> findAllEntities() {
		Query query = entityManager.createQuery("from QueryEntity");
		return query.getResultList();
	}
	
	private QueryEntity getEntityById(Long id) {
		QueryEntity entity = null;
		
		Query query = entityManager.createQuery( "SELECT distinct q from QueryEntity q "
				+ "LEFT OUTER JOIN FETCH q.orgStatuses "
				+ "where q.id = :entityid) ", 
				QueryEntity.class );
		
		query.setParameter("entityid", id);
		List<QueryEntity> results = query.getResultList();
		if(results.size() != 0) {
			entity = results.get(0);
		}
		return entity;
	}
	
	public Boolean hasActiveOrganizations(Long queryId) {		
		Query query = entityManager.createQuery( "SELECT count(org) "
				+ "FROM QueryOrganizationEntity org " 
				+ "WHERE org.queryId = :queryId " 
				+ "AND UPPER(status) = 'ACTIVE'", Long.class);
		
		query.setParameter("queryId", queryId);
		Long activeCount = (Long)query.getSingleResult();
		logger.info("Query " + queryId + " has " + activeCount + " ACTIVE statuses.");
		return activeCount > 0;
	}
	
	private QueryOrganizationEntity getQueryStatusById(Long id) {
		QueryOrganizationEntity entity = null;
		
		Query query = entityManager.createQuery( "SELECT distinct q from QueryOrganizationEntity q "
				+ "LEFT OUTER JOIN FETCH q.org " 
				+ "LEFT OUTER JOIN FETCH q.results "
				+ "where q.id = :entityid) ", 
				QueryOrganizationEntity.class );
		
		query.setParameter("entityid", id);
		List<QueryOrganizationEntity> result = query.getResultList();
		if(result.size() == 1) {
			entity = result.get(0);
		}
		
		return entity;
	}
	
	private List<QueryEntity> getEntitiesByUser(String user) {		
		Query query = entityManager.createQuery( "SELECT distinct q from QueryEntity q "
				+ "LEFT OUTER JOIN FETCH q.orgStatuses "
				+ "where q.userId = :userId) ", 
				QueryEntity.class );
		
		query.setParameter("userId", user);
		List<QueryEntity> result = query.getResultList();
		return result;
	}
	
	private List<QueryEntity> getEntitiesByUserAndStatus(String user, String status) {		
		Query query = entityManager.createQuery( "SELECT distinct q from QueryEntity q "
				+ "LEFT OUTER JOIN FETCH q.orgStatuses "
				+ "where q.userId = :userId "
				+ "and q.status = :status) ", 
				QueryEntity.class );
		
		query.setParameter("userId", user);
		query.setParameter("status", status);
		List<QueryEntity> result = query.getResultList();
		return result;
	}
}