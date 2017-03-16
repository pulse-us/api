package gov.ca.emsa.pulse.broker.dao.impl;

import gov.ca.emsa.pulse.broker.dao.QueryDAO;
import gov.ca.emsa.pulse.broker.dto.QueryDTO;
import gov.ca.emsa.pulse.broker.dto.QueryEndpointMapDTO;
import gov.ca.emsa.pulse.broker.entity.QueryEntity;
import gov.ca.emsa.pulse.broker.entity.QueryEndpointMapEntity;
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
import gov.ca.emsa.pulse.common.domain.QueryEndpointStatus;
import gov.ca.emsa.pulse.broker.entity.QueryEndpointStatusEntity;
import gov.ca.emsa.pulse.broker.entity.QueryStatusEntity;

@Repository
public class QueryDAOImpl extends BaseDAOImpl implements QueryDAO {
	private static final Logger logger = LogManager.getLogger(QueryDAOImpl.class);

	@Autowired QueryStatusDAO statusDao;
	
	@Override
	public QueryDTO create(QueryDTO dto) {
		
		QueryEntity query = new QueryEntity();
		query.setUserId(dto.getUserId());
		QueryStatusEntity queryStatus = statusDao.getQueryStatusByName(dto.getStatus() != null ? dto.getStatus().name() : QueryStatus.Active.name());
		query.setStatus(queryStatus);
		if(queryStatus != null) {
			query.setStatusId(queryStatus.getId());
		}
		query.setTerms(dto.getTerms());
		query.setLastReadDate(new Date());
		entityManager.persist(query);
		
		if(dto.getEndpointMaps() != null && dto.getEndpointMaps().size() > 0) {
			for(QueryEndpointMapDTO queryEndpointMap : dto.getEndpointMaps()) {
				QueryEndpointMapEntity queryEndpointToInsert = new QueryEndpointMapEntity();
				queryEndpointToInsert.setEndpointId(queryEndpointMap.getEndpointId());
				queryEndpointToInsert.setQueryId(query.getId());
				queryEndpointToInsert.setStartDate(new Date());
				QueryEndpointStatusEntity status = statusDao.getQueryEndpointStatusByName(QueryEndpointStatus.Active.name());
				queryEndpointToInsert.setStatusId(status == null ? null : status.getId());
				queryEndpointToInsert.setStatus(status);
				entityManager.persist(queryEndpointToInsert);
				query.getEndpointStatuses().add(queryEndpointToInsert);
			}
		}
		
		entityManager.flush();
		entityManager.clear();
		return new QueryDTO(query);
	}

	@Override
	public QueryEndpointMapDTO getQueryEndpointById(Long queryEndpointId) {
		QueryEndpointMapEntity entity = findQueryEndpointById(queryEndpointId);
		if(entity != null) {
			return new QueryEndpointMapDTO(entity);
		}
		return null;
	}
	
	@Override
	public QueryEndpointMapDTO createQueryEndpointMap(QueryEndpointMapDTO queryEndpointMapDto) {
		QueryEndpointMapEntity toInsert = new QueryEndpointMapEntity();
		toInsert.setEndpointId(queryEndpointMapDto.getEndpointId());
		toInsert.setQueryId(queryEndpointMapDto.getQueryId());
		toInsert.setStartDate(new Date());
		QueryEndpointStatusEntity status = statusDao.getQueryEndpointStatusByName(QueryEndpointStatus.Active.name());
		toInsert.setStatusId(status == null ? null : status.getId());
		entityManager.persist(toInsert);
		entityManager.flush();
		entityManager.clear();
		return new QueryEndpointMapDTO(toInsert);
	}
	
	@Override
	public QueryEndpointMapDTO updateQueryEndpointMap(QueryEndpointMapDTO newQueryEndpointMap) {
		logger.info("Update query location " + newQueryEndpointMap.getId() + " with status " + newQueryEndpointMap.getStatus());
		QueryEndpointMapEntity existingQueryEndpointMap = findQueryEndpointById(newQueryEndpointMap.getId());
		if(existingQueryEndpointMap != null) {
			existingQueryEndpointMap.setEndDate(newQueryEndpointMap.getEndDate());
			if((newQueryEndpointMap.getStatus() != null && 
				newQueryEndpointMap.getStatus() == QueryEndpointStatus.Closed) 
				||
				(existingQueryEndpointMap.getStatus() != null && 
				(existingQueryEndpointMap.getStatus().getStatus() != QueryEndpointStatus.Cancelled || 
				existingQueryEndpointMap.getStatus().getStatus() != QueryEndpointStatus.Closed))) {
				//always change the status if we are moving to Closed.
				//aside from that, don't change the status if it's currently Cancelled or Closed.
				QueryEndpointStatusEntity newStatus = 
						statusDao.getQueryEndpointStatusByName(newQueryEndpointMap.getStatus().name());
				existingQueryEndpointMap.setStatusId(newStatus == null ? null : newStatus.getId());
			} 
			entityManager.merge(existingQueryEndpointMap);
			entityManager.flush();
			logger.info("Updated query endpoint Status " + newQueryEndpointMap.getId());
			return new QueryEndpointMapDTO(existingQueryEndpointMap);
		} else {
			logger.info("Could not find a query endpoint map with ID " + newQueryEndpointMap.getId());
		}
		entityManager.clear();
		return null;
	}
	
	@Override
	public QueryDTO update(QueryDTO dto) {
		QueryEntity query = this.getEntityById(dto.getId());
		if(query.getStatus().getStatus() != QueryStatus.Closed) {
			logger.info("Set last read date for query " + dto.getId() + " to " + dto.getLastReadDate());
			query.setLastReadDate(dto.getLastReadDate());
			logger.info("Set status for query " + dto.getId() + " to " + dto.getStatus());
			QueryStatusEntity qStatus = statusDao.getQueryStatusByName(dto.getStatus().name());
			query.setStatus(qStatus);
			if(qStatus != null) {
				query.setStatusId(qStatus.getId());
			}
			//terms and user wouldn't change
			//location statuses should be updated separately in a manager
			entityManager.merge(query);
			entityManager.flush();
		} else {
			logger.warn("Attempted to update query " + dto.getId() + " but it is already marked 'Closed'. Not updating.");
		}
		entityManager.clear();
		return new QueryDTO(query);
	}

	@Override
	public void close(Long id) {
		try {
			QueryStatusEntity closedStatus = statusDao.getQueryStatusByName(QueryStatus.Closed.name());
			QueryEntity toDelete = getEntityById(id);
			if(closedStatus != null && toDelete != null) {
				toDelete.setStatus(closedStatus);
				toDelete.setStatusId(closedStatus.getId());
				entityManager.merge(toDelete);
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
	public List<QueryDTO> findAllForUserWithStatus(String userToken, List<QueryStatus> status) {
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
		//this was originally deleting queries but since we want to  keep
		//them around for statistics, it's just marking them as Closed instead
		Query query = entityManager.createQuery( "from QueryEntity qe "
				+ " WHERE qe.lastReadDate <= :cacheDate");
		
		query.setParameter("cacheDate", oldestDate);
		List<QueryEntity> oldQueries = query.getResultList();
		if(oldQueries.size() > 0) {
			for(QueryEntity oldQuery : oldQueries) {
				close(oldQuery.getId());
				logger.info("Query Cleanup: Closed query with id " + oldQuery.getId());
			}
		} else {
			logger.info("Query Cleanup: Closed 0 queries.");
		}
	}
	
	private QueryEntity getEntityById(Long id) {
		//the status entity was being cached and calling clear here
		//forces it to refresh based on the query we run
		entityManager.clear();
		
		QueryEntity entity = null;
		Query query = entityManager.createQuery( "SELECT distinct q from QueryEntity q "
				+ "LEFT OUTER JOIN FETCH q.status "
				+ "LEFT OUTER JOIN FETCH q.endpointStatuses statuses "
				+ "LEFT OUTER JOIN FETCH statuses.status status "
				+ "where q.id = :entityid) ", 
				QueryEntity.class );
		
		query.setParameter("entityid", id);
		List<QueryEntity> results = query.getResultList();
		if(results.size() != 0) {
			entity = results.get(0);
		}
		return entity;
	}
	
	public Boolean hasActiveLocations(Long queryId) {		
		QueryEndpointStatusEntity statusEntity = statusDao.getQueryEndpointStatusByName(QueryEndpointStatus.Active.name());
		if(statusEntity == null) {
			return Boolean.FALSE;
		}
		Query query = entityManager.createQuery( "SELECT count(map) "
				+ "FROM QueryEndpointMapEntity map " 
				+ "WHERE map.queryId = :queryId " 
				+ "AND statusId = :statusId", Long.class);
		
		query.setParameter("queryId", queryId);
		query.setParameter("statusId", statusEntity.getId());
		Long activeCount = (Long)query.getSingleResult();
		logger.info("Query " + queryId + " has " + activeCount + " ACTIVE statuses.");
		return activeCount > 0;
	}
	
	private QueryEndpointMapEntity findQueryEndpointById(Long id) {
		QueryEndpointMapEntity entity = null;
		
		Query query = entityManager.createQuery( "SELECT distinct q from QueryEndpointMapEntity q "
				+ "LEFT OUTER JOIN FETCH q.endpoint " 
				+ "LEFT OUTER JOIN FETCH q.results "
				+ "JOIN FETCH q.status "
				+ "where q.id = :entityid) ", 
				QueryEndpointMapEntity.class );
		
		query.setParameter("entityid", id);
		List<QueryEndpointMapEntity> result = query.getResultList();
		if(result.size() == 1) {
			entity = result.get(0);
		}
		
		return entity;
	}
	
	private List<QueryEntity> getEntitiesByUser(String user) {
		Query query = entityManager.createQuery( "SELECT distinct q from QueryEntity q "
				+ "LEFT OUTER JOIN FETCH q.status "
				+ "LEFT OUTER JOIN FETCH q.endpointStatuses "
				+ "where q.userId = :userId) ", 
				QueryEntity.class );
		
		query.setParameter("userId", user);
		List<QueryEntity> result = query.getResultList();
		return result;
	}
	
	private List<QueryEntity> getEntitiesByUserAndStatus(String user, List<QueryStatus> statuses) {		
		Query query = entityManager.createQuery( "SELECT distinct q from QueryEntity q "
				+ "LEFT OUTER JOIN FETCH q.status "
				+ "LEFT OUTER JOIN FETCH q.endpointStatuses "
				+ "where q.userId = :userId "
				+ "and q.status.status IN (:status)) ", 
				QueryEntity.class );
		
		query.setParameter("userId", user);
		query.setParameter("status", statuses);
		List<QueryEntity> result = query.getResultList();
		return result;
	}
}