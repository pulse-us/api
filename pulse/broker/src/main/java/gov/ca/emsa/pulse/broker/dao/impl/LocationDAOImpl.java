package gov.ca.emsa.pulse.broker.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityExistsException;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import gov.ca.emsa.pulse.broker.dao.LocationAddressLineDAO;
import gov.ca.emsa.pulse.broker.dao.LocationDAO;
import gov.ca.emsa.pulse.broker.dto.LocationDTO;
import gov.ca.emsa.pulse.broker.entity.LocationAddressLineEntity;
import gov.ca.emsa.pulse.broker.entity.LocationEntity;
import gov.ca.emsa.pulse.broker.entity.LocationStatusEntity;

@Repository
public class LocationDAOImpl extends BaseDAOImpl implements LocationDAO {
	private static final Logger logger = LogManager.getLogger(LocationDAOImpl.class);

	@Autowired LocationAddressLineDAO addressLineDao;
	
	public LocationDTO create(LocationDTO dto) throws EntityExistsException{
		LocationEntity entity = new LocationEntity();
		entity.setName(dto.getName().trim());
		entity.setDescription(dto.getDescription());
		entity.setParentOrganizationName(dto.getParentOrgName());
		entity.setType(dto.getType());
		if(dto.getStatus() != null) {
			if(dto.getStatus().getId() != null) {
				entity.setLocationStatusId(dto.getStatus().getId());
			} else if(!StringUtils.isEmpty(dto.getStatus().getName())) {
				LocationStatusEntity statusEntity = getLocationStatusByName(dto.getStatus().getName());
				if(statusEntity != null) {
					entity.setLocationStatusId(statusEntity.getId());
				} else {
					logger.error("Could not find location status with name " + dto.getStatus().getName());
				}
			}
		}
		entity.setExternalId(dto.getExternalId());
		entity.setExternalLastUpdatedDate(dto.getExternalLastUpdateDate());
		entity.setCity(dto.getCity());
		entity.setState(dto.getState());
		entity.setZipcode(dto.getZipcode());
		if(!entity.hasRequiredFields()) {
			logger.error("Cannot insert entity because a required field was null or empty.");
			return null;
		}
		entityManager.persist(entity);
		entityManager.flush();
		
		//add all the address lines
		if(dto.getLines() != null && dto.getLines().size() > 0) {
			for(int i = 0; i < dto.getLines().size(); i++) {
				LocationAddressLineEntity addrLine = new LocationAddressLineEntity();
				addrLine.setLocationId(entity.getId());
				addrLine.setOrder(i);
				addrLine.setLine(dto.getLines().get(i).getLine());	
				entityManager.persist(addrLine);
				entityManager.flush();
				entity.getLines().add(addrLine);
			}
		}
		return new LocationDTO(entity);
	}

	public LocationDTO update(LocationDTO dto){
		LocationEntity entity =  getByExternalId(dto.getExternalId());
		if(entity == null) {
			return create(dto);
		} 
		entity.setName(dto.getName());
		entity.setDescription(dto.getDescription());
		entity.setParentOrganizationName(dto.getParentOrgName());
		entity.setType(dto.getType());
		if(dto.getStatus() != null) {
			if(dto.getStatus().getId() != null) {
				entity.setLocationStatusId(dto.getStatus().getId());
			} else if(!StringUtils.isEmpty(dto.getStatus().getName())) {
				LocationStatusEntity statusEntity = getLocationStatusByName(dto.getStatus().getName());
				if(statusEntity != null) {
					entity.setLocationStatusId(statusEntity.getId());
				} else {
					logger.error("Could not find location status with name " + dto.getStatus().getName());
				}
			}
		}
		entity.setExternalId(dto.getExternalId());
		entity.setExternalLastUpdatedDate(dto.getExternalLastUpdateDate());
		entity.setCity(dto.getCity());
		entity.setState(dto.getState());
		entity.setZipcode(dto.getZipcode());
		if(!entity.hasRequiredFields()) {
			logger.error("Cannot insert entity because a required field was null or empty.");
			return null;
		}
		entityManager.merge(entity);
		entityManager.flush();
		
		//delete all address lines and re-add them
		addressLineDao.deleteAllForLocation(dto.getId());
		entity.getLines().clear();
		if(dto.getLines() != null && dto.getLines().size() > 0) {
			for(int i = 0; i < dto.getLines().size(); i++) {
				LocationAddressLineEntity addrLine = new LocationAddressLineEntity();
				addrLine.setLocationId(entity.getId());
				addrLine.setOrder(i);
				addrLine.setLine(dto.getLines().get(i).getLine());	
				entityManager.persist(addrLine);
				entityManager.flush();
				entity.getLines().add(addrLine);
			}
		}
		return new LocationDTO(entity);
	}

	public void delete(LocationDTO locationDto) {
		LocationEntity toDelete = null;
		if(locationDto.getId() != null) {
			toDelete = getById(locationDto.getId());
		} else if(!StringUtils.isBlank(locationDto.getExternalId())) {
			toDelete = getByExternalId(locationDto.getExternalId());
		}
		
		if(toDelete != null) {
			entityManager.remove(toDelete);
			entityManager.flush();
		}
	}

	@Override
	public List<LocationDTO> findAll() {

		List<LocationEntity> entities = getAllEntities();
		List<LocationDTO> dtos = new ArrayList<>();

		for (LocationEntity entity : entities) {
			LocationDTO dto = new LocationDTO(entity);
			dtos.add(dto);
		}
		return dtos;
	}
	
	@Override
	public List<LocationDTO> findByName(String name) {
		List<LocationEntity> entities = getByName(name);

		List<LocationDTO> dtos = new ArrayList<>();
		for (LocationEntity entity : entities) {
			LocationDTO dto = new LocationDTO(entity);
			dtos.add(dto);
		}
		return dtos;
	}

	@Override
	public LocationDTO findById(Long id) {
		LocationEntity result = getById(id);
		if(result == null) {
			return null;
		}
		return new LocationDTO(result);
	}

	@Override
	public LocationDTO findByExternalId(String externalId) {
		LocationEntity result = getByExternalId(externalId);
		if(result == null) {
			return null;
		}
		return new LocationDTO(result);
	}
	
	private LocationStatusEntity getLocationStatusByName(String name) {
		LocationStatusEntity result = null;
		Query query = entityManager.createQuery("from LocationStatusEntity where UPPER(name) = :name",
				LocationStatusEntity.class);
		query.setParameter("name", name.toUpperCase());
		List<LocationStatusEntity> results = query.getResultList();
		if(results == null || results.size() == 0) {
			return null;
		}
		result = results.get(0);
		return result;
	}
	
	private LocationEntity getById(Long id) {
		Query query = entityManager.createQuery("SELECT DISTINCT loc "
				+ "FROM LocationEntity loc "
				+ "JOIN FETCH loc.locationStatus "
				+ "LEFT OUTER JOIN FETCH loc.lines " 
				+ "WHERE loc.id = :id", LocationEntity.class);
		query.setParameter("id", id);
		
		List<LocationEntity> result = query.getResultList();
		if(result != null && result.size() > 0) {
			return result.get(0);
		}
		return null;
	}
	
	private List<LocationEntity> getByName(String name) {
		Query query = entityManager.createQuery("SELECT DISTINCT loc "
				+ "FROM LocationEntity loc "
				+ "JOIN FETCH loc.locationStatus "
				+ "LEFT OUTER JOIN FETCH loc.lines " 
				+ "WHERE loc.name LIKE :name", LocationEntity.class);
		query.setParameter("name", name);
		
		return query.getResultList();
	}
	
	private LocationEntity getByExternalId(String externalId) {
		Query query = entityManager.createQuery("SELECT DISTINCT loc "
				+ "FROM LocationEntity loc "
				+ "JOIN FETCH loc.locationStatus "
				+ "LEFT OUTER JOIN FETCH loc.lines " 
				+ "WHERE loc.externalId = :externalId", LocationEntity.class);
		query.setParameter("externalId", externalId);
		
		List<LocationEntity> result = query.getResultList();
		if(result != null && result.size() > 0) {
			return result.get(0);
		}
		return null;
	}
	
	@Override
	public List<LocationEntity> getAllEntities() {
		Query query = entityManager.createQuery("SELECT DISTINCT loc "
				+ "FROM LocationEntity loc "
				+ "JOIN FETCH loc.locationStatus "
				+ "LEFT OUTER JOIN FETCH loc.lines " 
				+ "LEFT OUTER JOIN FETCH loc.endpoints endpoints "
				+ "LEFT OUTER JOIN FETCH endpoints.mimeTypes ", LocationEntity.class);
		return query.getResultList();
	}
}
