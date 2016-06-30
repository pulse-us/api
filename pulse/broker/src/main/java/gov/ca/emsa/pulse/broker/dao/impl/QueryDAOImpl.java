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
				+ "LEFT JOIN FETCH q.query "
				+ "where q.id = :entityid) ", 
				QueryOrganizationEntity.class );
		
		query.setParameter("entityid", queryOrgId);
		entity = (QueryOrganizationEntity)query.getSingleResult();
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
	public synchronized QueryOrganizationDTO updateQueryOrganization(QueryOrganizationDTO orgStatus) {
		QueryOrganizationEntity orgMap = this.getQueryStatusById(orgStatus.getId());
		if(orgStatus.getStatus().equalsIgnoreCase(QueryStatus.COMPLETE.name())) {
			orgMap.setEndDate(new Date());
			orgMap.setSuccess(orgStatus.getSuccess());
			orgMap.setStatus(QueryStatus.COMPLETE.name());
		} else {
			orgMap.setStatus(QueryStatus.ACTIVE.name());
		}
		orgMap = entityManager.merge(orgMap);
		
		//see if the entire query is complete
		QueryEntity query = this.getEntityById(orgStatus.getQueryId());
		if(query.getOrgStatuses() != null && query.getOrgStatuses().size() > 0) {
			int completeCount = 0;
			for(QueryOrganizationEntity orgMapEntity : query.getOrgStatuses()) {
				if(orgMapEntity.getStatus().equalsIgnoreCase(QueryStatus.COMPLETE.name())) {
					completeCount++;
				} 
			}
			
			if(completeCount == query.getOrgStatuses().size()) {
				query.setStatus(QueryStatus.COMPLETE.name());
				query = entityManager.merge(query);
			}
		}
		
		entityManager.flush();
		return new QueryOrganizationDTO(orgMap);
	}
	
	@Override
	public QueryDTO update(QueryDTO dto) {
		QueryEntity query = this.getEntityById(dto.getId());
		query.setLastReadDate(dto.getLastReadDate());
		//terms and user wouldn't change
		
		//check the org statuses
		if(dto.getOrgStatuses() != null && dto.getOrgStatuses().size() > 0) {
			int completeCount = 0;
			for(QueryOrganizationDTO orgStatus : dto.getOrgStatuses()) {
				QueryOrganizationEntity orgMap = this.getQueryStatusById(orgStatus.getId());
				if(orgMap == null || orgMap.getId() == null) {
					//create 
					orgMap = new QueryOrganizationEntity();
					orgMap.setOrganizationId(orgStatus.getOrgId());
					orgMap.setQueryId(query.getId());
					orgMap.setStatus(QueryStatus.ACTIVE.name());
					orgMap.setStartDate(new Date());
					query.getOrgStatuses().add(orgMap);
					entityManager.persist(orgMap);
				} else {
					//update - org and query wouldn't change
					if(orgStatus.getStatus().equalsIgnoreCase(QueryStatus.COMPLETE.name())) {
						orgMap.setEndDate(new Date());
						orgMap.setSuccess(orgStatus.getSuccess());
						orgMap.setStatus(QueryStatus.COMPLETE.name());
						completeCount++;
					} else {
						orgMap.setStatus(QueryStatus.ACTIVE.name());
					}
					orgMap = entityManager.merge(orgMap);
				}
			}
			
			if(completeCount == dto.getOrgStatuses().size()) {
				query.setStatus(QueryStatus.COMPLETE.name());
				query = entityManager.merge(query);
			}
		}
		
		entityManager.flush();
		return new QueryDTO(query);
	}

	@Override
	public void delete(Long id) {
		QueryEntity toDelete = getEntityById(id);
		entityManager.remove(toDelete);
		entityManager.flush();
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
		
		if (qe != null){
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
		
		for(QueryEntity oldQuery : oldQueries) {
			if(oldQuery.getOrgStatuses() == null || oldQuery.getOrgStatuses().size() == 0) {
				delete(oldQuery.getId());
				logger.info("Deleted query with id " + oldQuery.getId() +" from the cache.");
			}
		}
	}
	
	private List<QueryEntity> findAllEntities() {
		Query query = entityManager.createQuery("from QueryEntity");
		return query.getResultList();
	}
	
	private QueryEntity getEntityById(Long id) {
		QueryEntity entity = null;
		
		Query query = entityManager.createQuery( "SELECT q from QueryEntity q "
				+ "LEFT OUTER JOIN FETCH q.orgStatuses "
				+ "where q.id = :entityid) ", 
				QueryEntity.class );
		
		query.setParameter("entityid", id);
		entity = (QueryEntity)query.getSingleResult();
		return entity;
	}
	
	private QueryOrganizationEntity getQueryStatusById(Long id) {
		QueryOrganizationEntity entity = null;
		
		Query query = entityManager.createQuery( "SELECT q from QueryOrganizationEntity q "
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
		Query query = entityManager.createQuery( "SELECT q from QueryEntity q "
				+ "LEFT OUTER JOIN FETCH q.orgStatuses "
				+ "where q.userId = :userId) ", 
				QueryEntity.class );
		
		query.setParameter("userId", user);
		List<QueryEntity> result = query.getResultList();
		return result;
	}
	
	private List<QueryEntity> getEntitiesByUserAndStatus(String user, String status) {		
		Query query = entityManager.createQuery( "SELECT q from QueryEntity q "
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