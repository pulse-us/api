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
import gov.ca.emsa.pulse.broker.entity.QueryOrganizationStatusMap;

@Repository
public class QueryDAOImpl extends BaseDAOImpl implements QueryDAO {
	private static final Logger logger = LogManager.getLogger(QueryDAOImpl.class);
	
	@Override
	public QueryDTO create(QueryDTO dto) {
		
		QueryEntity query = new QueryEntity();
		query.setUserToken(dto.getUserToken());
		query.setStatus(QueryStatus.ACTIVE.name());
		query.setTerms(dto.getTerms());
		entityManager.persist(query);
		
		if(dto.getOrgStatuses() != null && dto.getOrgStatuses().size() > 0) {
			for(QueryOrganizationDTO orgStatus : dto.getOrgStatuses()) {
				QueryOrganizationStatusMap orgMap = new QueryOrganizationStatusMap();
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

	public QueryOrganizationDTO createQueryOrganization(QueryOrganizationDTO orgStatus) {
		QueryOrganizationStatusMap orgMap = new QueryOrganizationStatusMap();
		orgMap.setOrganizationId(orgStatus.getOrgId());
		orgMap.setQueryId(orgStatus.getQueryId());
		orgMap.setStatus(QueryStatus.ACTIVE.name());
		orgMap.setStartDate(new Date());
		entityManager.persist(orgMap);
		return new QueryOrganizationDTO(orgMap);
	}
	
	@Override
	public synchronized QueryOrganizationDTO updateQueryOrganization(QueryOrganizationDTO orgStatus) {
		QueryOrganizationStatusMap orgMap = this.getQueryStatusById(orgStatus.getId());
		if(orgStatus.getStatus().equalsIgnoreCase(QueryStatus.COMPLETE.name())) {
			orgMap.setEndDate(new Date());
			orgMap.setFromCache(orgStatus.getFromCache());
			orgMap.setSuccess(orgStatus.getSuccess());
			orgMap.setStatus(QueryStatus.COMPLETE.name());
		} else {
			orgMap.setFromCache(orgStatus.getFromCache());
			orgMap.setStatus(QueryStatus.ACTIVE.name());
		}
		orgMap = entityManager.merge(orgMap);
		
		//see if the entire query is complete
		QueryEntity query = this.getEntityById(orgStatus.getQueryId());
		if(query.getOrgStatuses() != null && query.getOrgStatuses().size() > 0) {
			int completeCount = 0;
			for(QueryOrganizationStatusMap orgMapEntity : query.getOrgStatuses()) {
				if(orgMapEntity.getStatus().equalsIgnoreCase(QueryStatus.COMPLETE.name())) {
					completeCount++;
				} 
			}
			
			if(completeCount == query.getOrgStatuses().size()) {
				query.setStatus(QueryStatus.COMPLETE.name());
				query = entityManager.merge(query);
			}
		}
		
		return new QueryOrganizationDTO(orgMap);
	}
	
	@Override
	public QueryDTO update(QueryDTO dto) {
		QueryEntity query = this.getEntityById(dto.getId());
		//terms and user wouldn't change
		
		//check the org statuses
		if(dto.getOrgStatuses() != null && dto.getOrgStatuses().size() > 0) {
			int completeCount = 0;
			for(QueryOrganizationDTO orgStatus : dto.getOrgStatuses()) {
				QueryOrganizationStatusMap orgMap = this.getQueryStatusById(orgStatus.getId());
				if(orgMap == null || orgMap.getId() == null) {
					//create 
					orgMap = new QueryOrganizationStatusMap();
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
						orgMap.setFromCache(orgStatus.getFromCache());
						orgMap.setSuccess(orgStatus.getSuccess());
						orgMap.setStatus(QueryStatus.COMPLETE.name());
						completeCount++;
					} else {
						orgMap.setFromCache(orgStatus.getFromCache());
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
	
	private QueryOrganizationStatusMap getQueryStatusById(Long id) {
		QueryOrganizationStatusMap entity = null;
		
		Query query = entityManager.createQuery( "SELECT q from QueryOrganizationStatusMap q "
				+ "where q.id = :entityid) ", 
				QueryOrganizationStatusMap.class );
		
		query.setParameter("entityid", id);
		List<QueryOrganizationStatusMap> result = query.getResultList();
		if(result.size() == 1) {
			entity = result.get(0);
		}
		
		return entity;
	}
	
	private List<QueryEntity> getEntitiesByUser(String user) {		
		Query query = entityManager.createQuery( "SELECT q from QueryEntity q "
				+ "LEFT OUTER JOIN FETCH q.orgStatuses "
				+ "where q.userToken = :userToken) ", 
				QueryEntity.class );
		
		query.setParameter("userToken", user);
		List<QueryEntity> result = query.getResultList();
		return result;
	}
	
	private List<QueryEntity> getEntitiesByUserAndStatus(String user, String status) {		
		Query query = entityManager.createQuery( "SELECT q from QueryEntity q "
				+ "LEFT OUTER JOIN FETCH q.orgStatuses "
				+ "where q.userToken = :userToken "
				+ "and q.status = :status) ", 
				QueryEntity.class );
		
		query.setParameter("userToken", user);
		query.setParameter("status", status);
		List<QueryEntity> result = query.getResultList();
		return result;
	}
}