package gov.ca.emsa.pulse.broker.dao.impl;

import gov.ca.emsa.pulse.broker.dao.QueryDAO;
import gov.ca.emsa.pulse.broker.dto.QueryDTO;
import gov.ca.emsa.pulse.broker.dto.QueryOrganizationDTO;
import gov.ca.emsa.pulse.broker.entity.QueryEntity;
import gov.ca.emsa.pulse.broker.entity.QueryOrganizationEntity;
import gov.ca.emsa.pulse.common.domain.QueryStatus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import gov.ca.emsa.pulse.broker.dao.QueryStatusDAO;
import gov.ca.emsa.pulse.common.domain.QueryOrganizationStatus;
import gov.ca.emsa.pulse.broker.entity.QueryOrganizationStatusEntity;

@Repository
public class QueryDAOImpl extends BaseDAOImpl implements QueryDAO {
	private static final Logger logger = LogManager.getLogger(QueryDAOImpl.class);

	@Autowired QueryStatusDAO statusDao;
	
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
				orgMap.setStartDate(new Date());
				QueryOrganizationStatusEntity status = statusDao.getStatusByName(QueryOrganizationStatus.Active.name());
				orgMap.setStatusId(status == null ? null : status.getId());
				orgMap.setStatus(status);
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
				+ "JOIN FETCH q.status "
				+ "where q.id = :entityid) ", 
				QueryOrganizationEntity.class );
		
		query.setParameter("entityid", queryOrgId);
		List<QueryOrganizationEntity> results = query.getResultList();
		if(results.size() != 0) {
			entity = results.get(0);
		}
		return new QueryOrganizationDTO(entity);
	}
	
	public QueryOrganizationDTO getQueryOrganizationByQueryAndOrg(Long queryId, Long orgId) {
		QueryOrganizationEntity entity = null;
		
		Query query = entityManager.createQuery( "SELECT q from QueryOrganizationEntity q "
				+ "JOIN FETCH q.status "
				+ "where q.queryId = :queryId " 
				+ "AND q.organizationId = :orgId", 
				QueryOrganizationEntity.class );
		
		query.setParameter("queryId", queryId);
		query.setParameter("orgId", orgId);
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
		orgMap.setStartDate(new Date());
		QueryOrganizationStatusEntity status = statusDao.getStatusByName(QueryOrganizationStatus.Active.name());
		orgMap.setStatusId(status == null ? null : status.getId());
		entityManager.persist(orgMap);
		return new QueryOrganizationDTO(orgMap);
	}
	
	@Override
	public QueryOrganizationDTO updateQueryOrganization(QueryOrganizationDTO newOrgMap) {
		logger.info("Update query organization " + newOrgMap.getId() + " with status " + newOrgMap.getStatus());
		QueryOrganizationEntity existingOrgMap = this.getQueryStatusById(newOrgMap.getId());
		existingOrgMap.setEndDate(newOrgMap.getEndDate());
		if(existingOrgMap.getStatus() != null && 
				existingOrgMap.getStatus().getStatus() != QueryOrganizationStatus.Cancelled) {
			//don't change the status if it was previously cancelled.
			QueryOrganizationStatusEntity newStatus = 
					statusDao.getStatusByName(newOrgMap.getStatus().name());
			existingOrgMap.setStatusId(newStatus == null ? null : newStatus.getId());
		} 
		existingOrgMap = entityManager.merge(existingOrgMap);
		entityManager.flush();
		logger.info("Updated orgStatus " + newOrgMap.getId());
		return new QueryOrganizationDTO(existingOrgMap);
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
		//the status entity was being cached and calling clear here
		//forces it to refresh based on the query we run
		entityManager.clear();
		
		QueryEntity entity = null;
		Query query = entityManager.createQuery( "SELECT distinct q from QueryEntity q "
				+ "LEFT OUTER JOIN FETCH q.orgStatuses orgs "
				+ "LEFT OUTER JOIN FETCH orgs.status status "
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
		QueryOrganizationStatusEntity statusEntity = statusDao.getStatusByName(QueryOrganizationStatus.Active.name());
		if(statusEntity == null) {
			return Boolean.FALSE;
		}
		Query query = entityManager.createQuery( "SELECT count(org) "
				+ "FROM QueryOrganizationEntity org " 
				+ "WHERE org.queryId = :queryId " 
				+ "AND statusId = :statusId", Long.class);
		
		query.setParameter("queryId", queryId);
		query.setParameter("statusId", statusEntity.getId());
		Long activeCount = (Long)query.getSingleResult();
		logger.info("Query " + queryId + " has " + activeCount + " ACTIVE statuses.");
		return activeCount > 0;
	}
	
	private QueryOrganizationEntity getQueryStatusById(Long id) {
		QueryOrganizationEntity entity = null;
		
		Query query = entityManager.createQuery( "SELECT distinct q from QueryOrganizationEntity q "
				+ "LEFT OUTER JOIN FETCH q.org " 
				+ "LEFT OUTER JOIN FETCH q.results "
				+ "JOIN FETCH q.status "
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
		entityManager.clear();
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