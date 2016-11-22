package gov.ca.emsa.pulse.broker.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityExistsException;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import gov.ca.emsa.pulse.broker.dao.LocationAddressLineDAO;
import gov.ca.emsa.pulse.broker.dao.LocationDAO;
import gov.ca.emsa.pulse.broker.dto.LocationDTO;
import gov.ca.emsa.pulse.broker.dto.LocationEndpointDTO;
import gov.ca.emsa.pulse.broker.entity.LocationAddressLineEntity;
import gov.ca.emsa.pulse.broker.entity.LocationEndpointEntity;
import gov.ca.emsa.pulse.broker.entity.LocationEntity;

@Repository
public class LocationDAOImpl extends BaseDAOImpl implements LocationDAO {
	@Autowired LocationAddressLineDAO addressLineDao;
	
	public LocationDTO create(LocationDTO dto) throws EntityExistsException{
		LocationEntity entity = new LocationEntity();
		entity.setName(dto.getName());
		entity.setDescription(dto.getDescription());
		entity.setParentOrganizationName(dto.getParentOrgName());
		entity.setType(dto.getType());
		if(dto.getStatus() != null) {
			entity.setLocationStatusId(dto.getStatus().getId());
		}
		entity.setExternalId(dto.getExternalId());
		entity.setExternalLastUpdatedDate(dto.getExternalLastUpdateDate());
		entity.setCity(dto.getCity());
		entity.setState(dto.getState());
		entity.setZipcode(dto.getZipcode());
		entity.setCountry(dto.getCountry());
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
				entityManager.persist(endpointEntity);
				entityManager.flush();
				entity.getEndpoints().add(endpointEntity);
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
			entity.setLocationStatusId(dto.getStatus().getId());
		}
		entity.setExternalId(dto.getExternalId());
		entity.setExternalLastUpdatedDate(dto.getExternalLastUpdateDate());
		entity.setCity(dto.getCity());
		entity.setState(dto.getState());
		entity.setZipcode(dto.getZipcode());
		entity.setCountry(dto.getCountry());
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
		
		//look for endpoints to remove or update
		for(LocationEndpointEntity currEndpointEntity : entity.getEndpoints()) {
			String externalId = currEndpointEntity.getExternalId();
			LocationEndpointDTO updatedEndpointDto = null;
			for(LocationEndpointDTO endpointDto : dto.getEndpoints()) {
				if(endpointDto.getExternalId().equals(externalId)) {
					updatedEndpointDto = endpointDto;
				}
			}
			
			if(updatedEndpointDto != null) {
				//the existing endpoint was found - update data
				currEndpointEntity.setAdapter(updatedEndpointDto.getAdapter());
				if(updatedEndpointDto.getEndpointStatus() != null) {
					currEndpointEntity.setEndpointStatusId(updatedEndpointDto.getEndpointStatus().getId());
				}
				if(updatedEndpointDto.getEndpointType() != null) {
					currEndpointEntity.setEndpointTypeId(updatedEndpointDto.getEndpointType().getId());
				}
				currEndpointEntity.setExternalId(updatedEndpointDto.getExternalId());
				currEndpointEntity.setExternalLastUpdateDate(updatedEndpointDto.getExternalLastUpdateDate());
				currEndpointEntity.setLocationId(entity.getId());
				currEndpointEntity.setPayloadFormat(updatedEndpointDto.getPayloadFormat());
				currEndpointEntity.setPayloadType(updatedEndpointDto.getPayloadType());
				currEndpointEntity.setPublicKey(updatedEndpointDto.getPublicKey());
				currEndpointEntity.setUrl(updatedEndpointDto.getUrl());
				entityManager.merge(currEndpointEntity);
				entityManager.flush();
			} else {
				//the existing endpoint was not found in the update
				entityManager.remove(currEndpointEntity);
				entityManager.flush();
				entity.getEndpoints().remove(currEndpointEntity);
			}
		}
		
		//look for ones to add
		for(LocationEndpointDTO endpointDto : dto.getEndpoints()) {
			boolean alreadyExists = false;
			for(LocationEndpointEntity currEndpointEntity : entity.getEndpoints()) {
				if(currEndpointEntity.getExternalId().equals(endpointDto.getExternalId())) {
					alreadyExists = true;
				}
			}
			if(!alreadyExists) {
				LocationEndpointEntity endpointEntity = convert(endpointDto);
				endpointEntity.setLocationId(entity.getId());
				entityManager.persist(endpointEntity);
				entityManager.flush();
				entity.getEndpoints().add(endpointEntity);
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
		LocationEntity org = getById(id);
		if(org == null) {
			return null;
		}
		return new LocationDTO(org);
	}

	private LocationEntity getById(Long id) {
		Query query = entityManager.createQuery("SELECT DISTINCT loc "
				+ "FROM LocationEntity loc "
				+ "JOIN FETCH loc.locationStatus "
				+ "LEFT OUTER JOIN FETCH loc.lines " 
				+ "LEFT OUTER JOIN FETCH loc.endpoints "
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
				+ "LEFT OUTER JOIN FETCH loc.endpoints "
				+ "WHERE loc.name LIKE :name", LocationEntity.class);
		query.setParameter("name", name);
		
		return query.getResultList();
	}
	
	private LocationEntity getByExternalId(String externalId) {
		Query query = entityManager.createQuery("SELECT DISTINCT loc "
				+ "FROM LocationEntity loc "
				+ "JOIN FETCH loc.locationStatus "
				+ "LEFT OUTER JOIN FETCH loc.lines " 
				+ "LEFT OUTER JOIN FETCH loc.endpoints "
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
				+ "LEFT OUTER JOIN FETCH loc.endpoints "
				+ "WHERE loc.externalId = :externalId", LocationEntity.class);
		return query.getResultList();
	}
	
	private LocationEndpointEntity convert(LocationEndpointDTO endpointDto) {
		LocationEndpointEntity endpointEntity = new LocationEndpointEntity();
		endpointEntity.setAdapter(endpointDto.getAdapter());
		if(endpointDto.getEndpointStatus() != null) {
			endpointEntity.setEndpointStatusId(endpointDto.getEndpointStatus().getId());
		}
		if(endpointDto.getEndpointType() != null) {
			endpointEntity.setEndpointTypeId(endpointDto.getEndpointType().getId());
		}
		endpointEntity.setExternalId(endpointDto.getExternalId());
		endpointEntity.setExternalLastUpdateDate(endpointDto.getExternalLastUpdateDate());
		endpointEntity.setPayloadFormat(endpointDto.getPayloadFormat());
		endpointEntity.setPayloadType(endpointDto.getPayloadType());
		endpointEntity.setPublicKey(endpointDto.getPublicKey());
		endpointEntity.setUrl(endpointDto.getUrl());
		return endpointEntity;
	}
}
