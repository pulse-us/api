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
import gov.ca.emsa.pulse.broker.domain.EndpointTypeEnum;
import gov.ca.emsa.pulse.broker.dto.LocationDTO;
import gov.ca.emsa.pulse.broker.dto.LocationEndpointDTO;
import gov.ca.emsa.pulse.broker.dto.LocationEndpointMimeTypeDTO;
import gov.ca.emsa.pulse.broker.entity.EndpointStatusEntity;
import gov.ca.emsa.pulse.broker.entity.EndpointTypeEntity;
import gov.ca.emsa.pulse.broker.entity.LocationAddressLineEntity;
import gov.ca.emsa.pulse.broker.entity.LocationEndpointEntity;
import gov.ca.emsa.pulse.broker.entity.LocationEndpointMimeTypeEntity;
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
		
		//add all the endpoints
		if(dto.getEndpoints() != null && dto.getEndpoints().size() > 0) {
			for(LocationEndpointDTO endpointDto : dto.getEndpoints()) {
				LocationEndpointEntity endpointEntity = convert(endpointDto);
				endpointEntity.setLocationId(entity.getId());
				//insert the endpoint if required fields are set
				if(endpointEntity.hasRequiredFields()) {
					entityManager.persist(endpointEntity);
					entityManager.flush();
					entity.getEndpoints().add(endpointEntity);
					
					if(endpointEntity.getMimeTypes() != null) {
						for(LocationEndpointMimeTypeEntity mimeTypeEntity : endpointEntity.getMimeTypes()) {
							mimeTypeEntity.setEndpointId(endpointEntity.getId());
							entityManager.persist(mimeTypeEntity);
							entityManager.flush();
						}
					}
				}
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
		
		//it's very messy to figure out what to insert/update/delete so let's
		//just delete them all and do an insert
		deleteAllEndpointsForLocation(dto.getId());
		//add all the endpoints
		if(dto.getEndpoints() != null && dto.getEndpoints().size() > 0) {
			for(LocationEndpointDTO endpointDto : dto.getEndpoints()) {
				LocationEndpointEntity endpointEntity = convert(endpointDto);
				endpointEntity.setLocationId(entity.getId());
				//insert the endpoint if required fields are set
				if(endpointEntity.hasRequiredFields()) {
					entityManager.persist(endpointEntity);
					entityManager.flush();
					entity.getEndpoints().add(endpointEntity);
					
					if(endpointEntity.getMimeTypes() != null) {
						for(LocationEndpointMimeTypeEntity mimeTypeEntity : endpointEntity.getMimeTypes()) {
							mimeTypeEntity.setEndpointId(endpointEntity.getId());
							entityManager.persist(mimeTypeEntity);
							entityManager.flush();
						}
					}
				}
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
	public List<LocationDTO> findAllWithEndpointType(List<EndpointTypeEnum> types) {
		List<String> typeNames = new ArrayList<String>(types.size());
		for(EndpointTypeEnum type : types) {
			typeNames.add(type.getName());
		}
		List<LocationEntity> entities = getByEndpointTypes(typeNames);
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
	
	private void deleteAllEndpointsForLocation(Long locationId) {
		Query query = entityManager.createQuery("DELETE from LocationEndpointEntity where locationId = :locationId");
		query.setParameter("locationId", locationId);
		query.executeUpdate();
		entityManager.flush();
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
	
	private EndpointStatusEntity getEndpointStatusByName(String name) {
		EndpointStatusEntity result = null;
		Query query = entityManager.createQuery("from EndpointStatusEntity where UPPER(name) = :name",
				EndpointStatusEntity.class);
		query.setParameter("name", name.toUpperCase());
		List<EndpointStatusEntity> results = query.getResultList();
		if(results == null || results.size() == 0) {
			return null;
		}
		result = results.get(0);
		return result;
	}
	
	private EndpointTypeEntity getEndpointTypeByName(String name) {
		EndpointTypeEntity result = null;
		Query query = entityManager.createQuery("from EndpointTypeEntity where UPPER(name) = :name",
				EndpointTypeEntity.class);
		query.setParameter("name", name.toUpperCase());
		List<EndpointTypeEntity> results = query.getResultList();
		if(results == null || results.size() == 0) {
			return null;
		}
		result = results.get(0);
		return result;
	}
	
	private LocationEndpointMimeTypeEntity getEndpointMimeType(Long endpointId, String mimeType) {
		LocationEndpointMimeTypeEntity result = null;
		Query query = entityManager.createQuery("from LocationEndpointMimeTypeEntity "
				+ "WHERE endpointId = :endpointId "
				+ "UPPER(mimeType) = :mimeType ",
				LocationEndpointMimeTypeEntity.class);
		query.setParameter("endpointId", endpointId);
		query.setParameter("mimeType", mimeType.toUpperCase());
		List<LocationEndpointMimeTypeEntity> results = query.getResultList();
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
				+ "LEFT OUTER JOIN FETCH loc.endpoints endpoints "
				+ "LEFT OUTER JOIN FETCH endpoints.mimeTypes "
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
				+ "LEFT OUTER JOIN FETCH loc.endpoints endpoints "
				+ "LEFT OUTER JOIN FETCH endpoints.mimeTypes "
				+ "WHERE loc.name LIKE :name", LocationEntity.class);
		query.setParameter("name", name);
		
		return query.getResultList();
	}
	
	private LocationEntity getByExternalId(String externalId) {
		Query query = entityManager.createQuery("SELECT DISTINCT loc "
				+ "FROM LocationEntity loc "
				+ "JOIN FETCH loc.locationStatus "
				+ "LEFT OUTER JOIN FETCH loc.lines " 
				+ "LEFT OUTER JOIN FETCH loc.endpoints endpoints "
				+ "LEFT OUTER JOIN FETCH endpoints.mimeTypes "
				+ "WHERE loc.externalId = :externalId", LocationEntity.class);
		query.setParameter("externalId", externalId);
		
		List<LocationEntity> result = query.getResultList();
		if(result != null && result.size() > 0) {
			return result.get(0);
		}
		return null;
	}
	
	private List<LocationEntity> getByEndpointTypes(List<String> typeNames) {
		Query query = entityManager.createQuery("SELECT DISTINCT loc "
				+ "FROM LocationEntity loc "
				+ "JOIN FETCH loc.locationStatus "
				+ "LEFT OUTER JOIN FETCH loc.lines " 
				+ "LEFT OUTER JOIN FETCH loc.endpoints endpoints "
				+ "LEFT OUTER JOIN FETCH endpoints.mimeTypes "
				+ "WHERE endpoints.endpointType.name IN (:typeNames)", LocationEntity.class);
		query.setParameter("typeNames", typeNames);
		
		return query.getResultList();
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
	
	private LocationEndpointEntity convert(LocationEndpointDTO endpointDto) {
		LocationEndpointEntity endpointEntity = new LocationEndpointEntity();
		endpointEntity.setAdapter(endpointDto.getAdapter());
		if(endpointDto.getEndpointStatus() != null) {
			if(endpointDto.getEndpointStatus().getId() != null) {
				endpointEntity.setEndpointStatusId(endpointDto.getEndpointStatus().getId());
			} else if(!StringUtils.isEmpty(endpointDto.getEndpointStatus().getName())) {
				EndpointStatusEntity statusEntity = getEndpointStatusByName(endpointDto.getEndpointStatus().getName());
				if(statusEntity != null) {
					endpointEntity.setEndpointStatusId(statusEntity.getId());
				} else {
					logger.error("Could not find endpoint status with name " + endpointDto.getEndpointStatus().getName());
				}
			}
		}
		if(endpointDto.getEndpointType() != null) {
			if(endpointDto.getEndpointType().getId() != null) {
				endpointEntity.setEndpointTypeId(endpointDto.getEndpointType().getId());
			} else if(!StringUtils.isEmpty(endpointDto.getEndpointType().getName())) {
				EndpointTypeEntity typeEntity = getEndpointTypeByName(endpointDto.getEndpointType().getName());
				if(typeEntity != null) {
					endpointEntity.setEndpointTypeId(typeEntity.getId());
				} else {
					logger.error("Could not find endpoint type with name " + endpointDto.getEndpointType().getName());
				}
			}
		}
		endpointEntity.setExternalId(endpointDto.getExternalId());
		endpointEntity.setExternalLastUpdateDate(endpointDto.getExternalLastUpdateDate());
		if(endpointDto.getMimeTypes() != null && endpointDto.getMimeTypes().size() > 0) {
			for(LocationEndpointMimeTypeDTO mimetypeDto : endpointDto.getMimeTypes()) {
				if(endpointDto.getId() != null) {
					LocationEndpointMimeTypeEntity mtEntity = 
							getEndpointMimeType(endpointDto.getId(), mimetypeDto.getMimeType());
					if(mtEntity == null) {
						mtEntity = new LocationEndpointMimeTypeEntity();
						mtEntity.setMimeType(mimetypeDto.getMimeType());
						endpointEntity.getMimeTypes().add(mtEntity);
					} else {
						endpointEntity.getMimeTypes().add(mtEntity);
					}
				} else {
					LocationEndpointMimeTypeEntity mtEntity = new LocationEndpointMimeTypeEntity();
					mtEntity.setMimeType(mimetypeDto.getMimeType());
					endpointEntity.getMimeTypes().add(mtEntity);
				}
			}
		}
		endpointEntity.setPayloadType(endpointDto.getPayloadType());
		endpointEntity.setPublicKey(endpointDto.getPublicKey());
		endpointEntity.setUrl(endpointDto.getUrl());
		return endpointEntity;
	}
}
