package gov.ca.emsa.pulse.broker.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import gov.ca.emsa.pulse.broker.dao.LocationEndpointMapDAO;
import gov.ca.emsa.pulse.broker.dto.EndpointDTO;
import gov.ca.emsa.pulse.broker.dto.LocationDTO;
import gov.ca.emsa.pulse.broker.dto.LocationEndpointMapDTO;
import gov.ca.emsa.pulse.broker.entity.EndpointEntity;
import gov.ca.emsa.pulse.broker.entity.LocationEndpointMapEntity;
import gov.ca.emsa.pulse.broker.entity.LocationEntity;

@Repository
public class LocationEndpointMapDAOImpl extends BaseDAOImpl implements LocationEndpointMapDAO {
	private static final Logger logger = LogManager.getLogger(LocationEndpointMapDAOImpl.class);

	public LocationEndpointMapDTO create(Long locationId, Long endpointId) {
		LocationEndpointMapEntity entity = new LocationEndpointMapEntity();
		entity.setEndpointId(endpointId);
		entity.setLocationId(locationId);
		entityManager.persist(entity);
		entityManager.flush();
		entityManager.clear();
		return new LocationEndpointMapDTO(entity);
	}
	
	public boolean exists(Long locationId, Long endpointId) {
		Query query = entityManager.createQuery("SELECT map " 
				+ "FROM LocationEndpointMapEntity map " 
				+ "WHERE map.locationId = :locationId " 
				+ "AND map.endpointId = :endpointId", LocationEndpointMapEntity.class);
		query.setParameter("locationId", locationId);
		query.setParameter("endpointId", endpointId);
		
		List<LocationEndpointMapEntity> results = query.getResultList();
		return results != null && results.size() > 0;
	}
	
	public List<LocationEndpointMapDTO> findAll() {
		Query query = entityManager.createQuery("SELECT map " 
				+ "FROM LocationEndpointMapEntity map", LocationEndpointMapEntity.class);
		List<LocationEndpointMapEntity> results = query.getResultList();
		
		List<LocationEndpointMapDTO> dtos = new ArrayList<LocationEndpointMapDTO>();
		if(results == null) { return dtos; }
		for(LocationEndpointMapEntity result : results) {
			dtos.add(new LocationEndpointMapDTO(result));
		}
		return dtos;
	}
	
	public List<LocationDTO> findLocationsForEndpoint(Long endpointId) {
		Query query = entityManager.createQuery("SELECT loc " 
				+ "FROM LocationEntity loc "
				+ "INNER JOIN LocationEndpointMapEntity map " 
				+ "WHERE map.endpointId = :endpointId ", LocationEntity.class);
		query.setParameter("endpointId", endpointId);
		List<LocationEntity> results = query.getResultList();
		
		List<LocationDTO> dtos = new ArrayList<LocationDTO>();
		if(results == null) { return dtos; }
		for(LocationEntity result : results) {
			dtos.add(new LocationDTO(result));
		}
		return dtos;
	}
	
	public List<EndpointDTO> findEndpointsForLocation(Long locationId) {
		Query query = entityManager.createQuery("SELECT endpoint " 
				+ "FROM EndpointEntity endpoint "
				+ "INNER JOIN LocationEndpointMapEntity map " 
				+ "WHERE map.locationId = :locationId ", EndpointEntity.class);
		query.setParameter("locationId", locationId);
		List<EndpointEntity> results = query.getResultList();
		
		List<EndpointDTO> dtos = new ArrayList<EndpointDTO>();
		if(results == null) { return dtos; }
		for(EndpointEntity result : results) {
			dtos.add(new EndpointDTO(result));
		}
		return dtos;
	}
	
	public void delete(Long locationId, Long endpointId) {
		Query query = entityManager.createQuery("SELECT map " 
				+ "FROM LocationEndpointMapEntity map " 
				+ "WHERE map.locationId = :locationId " 
				+ "AND map.endpointId = :endpointId", LocationEndpointMapEntity.class);
		query.setParameter("locationId", locationId);
		query.setParameter("endpointId", endpointId);
		
		List<LocationEndpointMapEntity> results = query.getResultList();
		if(results != null && results.size() == 1) {
			entityManager.remove(results.get(0));
			entityManager.flush();
		}
		entityManager.clear();
	}
	
	public void delete(Long id) {
		Query query = entityManager.createQuery("SELECT map " 
				+ "FROM LocationEndpointMapEntity map " 
				+ "WHERE map.id = :id ", LocationEndpointMapEntity.class);
		query.setParameter("id", id);
		
		List<LocationEndpointMapEntity> results = query.getResultList();
		if(results != null && results.size() == 1) {
			entityManager.remove(results.get(0));
			entityManager.flush();
		}
		entityManager.clear();
	}
}
