package gov.ca.emsa.pulse.broker.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityExistsException;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import gov.ca.emsa.pulse.broker.dao.EndpointDAO;
import gov.ca.emsa.pulse.broker.domain.EndpointStatusEnum;
import gov.ca.emsa.pulse.broker.domain.EndpointTypeEnum;
import gov.ca.emsa.pulse.broker.dto.EndpointDTO;
import gov.ca.emsa.pulse.broker.dto.EndpointMimeTypeDTO;
import gov.ca.emsa.pulse.broker.entity.EndpointEntity;
import gov.ca.emsa.pulse.broker.entity.EndpointMimeTypeEntity;
import gov.ca.emsa.pulse.broker.entity.EndpointStatusEntity;
import gov.ca.emsa.pulse.broker.entity.EndpointTypeEntity;

@Repository
public class EndpointDAOImpl extends BaseDAOImpl implements EndpointDAO {
	private static final Logger logger = LogManager.getLogger(EndpointDAOImpl.class);
	
	public EndpointDTO create(EndpointDTO endpointDto) throws EntityExistsException {
		EndpointEntity endpointEntity = convert(endpointDto, null);
		//insert the endpoint if required fields are set
		if(endpointEntity.hasRequiredFields()) {
			entityManager.persist(endpointEntity);
			entityManager.flush();
			
			if(endpointEntity.getMimeTypes() != null && endpointEntity.getMimeTypes().size() > 0) {
				for(EndpointMimeTypeEntity mimeTypeEntity : endpointEntity.getMimeTypes()) {
					mimeTypeEntity.setEndpointId(endpointEntity.getId());
					entityManager.persist(mimeTypeEntity);
					entityManager.flush();
				}
			}
			entityManager.clear();
			return new EndpointDTO(endpointEntity);
		}
		return null;
	}

	public EndpointDTO update(EndpointDTO dto){
		EndpointEntity endpointEntity =  getByExternalId(dto.getExternalId());
		if(endpointEntity == null) {
			return create(dto);
		} 
		endpointEntity = convert(dto, endpointEntity);
		if(endpointEntity.hasRequiredFields()) {
			entityManager.merge(endpointEntity);
			entityManager.flush();
		}
		
		deleteEndpointMimeTypes(endpointEntity.getId());
		endpointEntity.getMimeTypes().clear();
		if(dto.getMimeTypes() != null && dto.getMimeTypes().size() > 0) {
			for(EndpointMimeTypeDTO mimeTypeDto : dto.getMimeTypes()) {
				EndpointMimeTypeEntity mtEntity = new EndpointMimeTypeEntity();
				mtEntity.setMimeType(mimeTypeDto.getMimeType());
				endpointEntity.getMimeTypes().add(mtEntity);
				mtEntity.setEndpointId(endpointEntity.getId());
				entityManager.persist(mtEntity);
				entityManager.flush();
			}
		}
		
		entityManager.clear();
		return new EndpointDTO(endpointEntity);
	}

	public void delete(EndpointDTO endpointDto) {
		EndpointEntity toDelete = null;
		if(endpointDto.getId() != null) {
			toDelete = getById(endpointDto.getId());
		} else if(!StringUtils.isBlank(endpointDto.getExternalId())) {
			toDelete = getByExternalId(endpointDto.getExternalId());
		}
		
		if(toDelete != null) {
			entityManager.remove(toDelete);
			entityManager.flush();
		}
		entityManager.clear();
	}

	@Override
	public List<EndpointDTO> findAll() {

		List<EndpointEntity> entities = getAllEntities();
		List<EndpointDTO> dtos = new ArrayList<EndpointDTO>();

		for (EndpointEntity entity : entities) {
			EndpointDTO dto = new EndpointDTO(entity);
			dtos.add(dto);
		}
		return dtos;
	}

	@Override 
	public List<EndpointDTO> findAllOfType(List<EndpointTypeEnum> types) {
		List<String> typeNames = new ArrayList<String>(types.size());
		for(EndpointTypeEnum type : types) {
			typeNames.add(type.getCode());
		}
		List<EndpointEntity> entities = getByEndpointTypes(typeNames);
		List<EndpointDTO> dtos = new ArrayList<EndpointDTO>();
		for (EndpointEntity entity : entities) {
			EndpointDTO dto = new EndpointDTO(entity);
			dtos.add(dto);
		}
		return dtos;
	}

	@Override 
	public List<EndpointDTO> findAllByStatusAndType(List<EndpointStatusEnum> statuses, List<EndpointTypeEnum> types) {
		List<String> typeNames = new ArrayList<String>(types.size());
		for(EndpointTypeEnum type : types) {
			typeNames.add(type.getCode().toUpperCase());
		}
		
		List<String> statusNames = new ArrayList<String>(statuses.size());
		for(EndpointStatusEnum status : statuses) {
			statusNames.add(status.getName().toUpperCase());
		}
		
		List<EndpointEntity> entities = getByEndpointStatusAndTypes(statusNames, typeNames);
		List<EndpointDTO> dtos = new ArrayList<EndpointDTO>();
		for (EndpointEntity entity : entities) {
			EndpointDTO dto = new EndpointDTO(entity);
			dtos.add(dto);
		}
		return dtos;
	}
	
	@Override
	public EndpointDTO findById(Long id) {
		EndpointEntity result = getById(id);
		if(result == null) {
			return null;
		}
		return new EndpointDTO(result);
	}

	@Override
	public EndpointDTO findByExternalId(String externalId) {
		EndpointEntity result = getByExternalId(externalId);
		if(result == null) {
			return null;
		}
		return new EndpointDTO(result);
	}
	
	@Override
	public EndpointDTO findByLocationIdAndType(Long locationId, EndpointStatusEnum status, EndpointTypeEnum type) {
		Query query = entityManager.createQuery("SELECT DISTINCT endpoint "
				+ "FROM EndpointEntity endpoint "
				+ "JOIN FETCH endpoint.endpointStatus endpointStatus "
				+ "JOIN FETCH endpoint.endpointType "
				+ "LEFT OUTER JOIN FETCH endpoint.mimeTypes "
				+ "LEFT OUTER JOIN FETCH endpoint.locationEndpointMaps locMaps "
				+ "LEFT OUTER JOIN FETCH locMaps.location loc "
				+ "WHERE loc.id = :locationId "
				+ "AND UPPER(endpoint.endpointType.code) = :typeCode "
				+ "AND UPPER(endpointStatus.name) = :endpointStatusName"
				, EndpointEntity.class);
		query.setParameter("typeCode", type.getCode().toUpperCase());
		query.setParameter("locationId", locationId);
		query.setParameter("endpointStatusName", status.getName().toUpperCase());
		
		List<EndpointEntity> endpoints = query.getResultList();
		if(endpoints != null && endpoints.size() > 0) {
			return new EndpointDTO(endpoints.get(0));
		}
		return null;
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
	
	private EndpointTypeEntity getEndpointTypeByCode(String code) {
		EndpointTypeEntity result = null;
		Query query = entityManager.createQuery("from EndpointTypeEntity where UPPER(code) = :code",
				EndpointTypeEntity.class);
		query.setParameter("code", code.toUpperCase());
		List<EndpointTypeEntity> results = query.getResultList();
		if(results == null || results.size() == 0) {
			return null;
		}
		result = results.get(0);
		return result;
	}
	
	private void deleteEndpointMimeTypes(Long endpointId) {
		Query query = entityManager.createQuery("from EndpointMimeTypeEntity "
				+ "WHERE endpointId = :endpointId ",
				EndpointMimeTypeEntity.class);
		query.setParameter("endpointId", endpointId);
		List<EndpointMimeTypeEntity> results = query.getResultList();
		for(EndpointMimeTypeEntity toDelete : results) {
			entityManager.remove(toDelete);
		}
		entityManager.flush();
	}
	
	private EndpointMimeTypeEntity getEndpointMimeType(Long endpointId, String mimeType) {
		EndpointMimeTypeEntity result = null;
		Query query = entityManager.createQuery("from EndpointMimeTypeEntity "
				+ "WHERE endpointId = :endpointId "
				+ "AND UPPER(mimeType) = :mimeType ",
				EndpointMimeTypeEntity.class);
		query.setParameter("endpointId", endpointId);
		query.setParameter("mimeType", mimeType.toUpperCase());
		List<EndpointMimeTypeEntity> results = query.getResultList();
		if(results == null || results.size() == 0) {
			return null;
		}
		result = results.get(0);
		return result;
	}
	
	private EndpointEntity getById(Long id) {
		Query query = entityManager.createQuery("SELECT DISTINCT endpoint "
				+ "FROM EndpointEntity endpoint "
				+ "JOIN FETCH endpoint.endpointStatus "
				+ "JOIN FETCH endpoint.endpointType "
				+ "LEFT OUTER JOIN FETCH endpoint.mimeTypes "
				+ "LEFT OUTER JOIN FETCH endpoint.locationEndpointMaps locMaps "
				+ "LEFT OUTER JOIN FETCH locMaps.location "
				+ "WHERE endpoint.id = :id", EndpointEntity.class);
		query.setParameter("id", id);
		
		List<EndpointEntity> result = query.getResultList();
		if(result != null && result.size() > 0) {
			return result.get(0);
		}
		return null;
	}
	
	private EndpointEntity getByExternalId(String externalId) {
		Query query = entityManager.createQuery("SELECT DISTINCT endpoint "
				+ "FROM EndpointEntity endpoint "
				+ "JOIN FETCH endpoint.endpointStatus "
				+ "JOIN FETCH endpoint.endpointType "
				+ "LEFT OUTER JOIN FETCH endpoint.mimeTypes "
				+ "LEFT OUTER JOIN FETCH endpoint.locationEndpointMaps locMaps "
				+ "LEFT OUTER JOIN FETCH locMaps.location "
				+ "WHERE endpoint.externalId = :externalId", EndpointEntity.class);
		query.setParameter("externalId", externalId);
		
		List<EndpointEntity> result = query.getResultList();
		if(result != null && result.size() > 0) {
			return result.get(0);
		}
		return null;
	}
	
	private List<EndpointEntity> getByEndpointTypes(List<String> typeNames) {
		Query query = entityManager.createQuery("SELECT DISTINCT endpoint "
				+ "FROM EndpointEntity endpoint "
				+ "JOIN FETCH endpoint.endpointStatus "
				+ "JOIN FETCH endpoint.endpointType "
				+ "LEFT OUTER JOIN FETCH endpoint.mimeTypes "
				+ "LEFT OUTER JOIN FETCH endpoint.locationEndpointMaps locMaps "
				+ "LEFT OUTER JOIN FETCH locMaps.location "
				+ "WHERE endpoint.endpointType.code IN (:typeNames)", EndpointEntity.class);
		query.setParameter("typeNames", typeNames);
		
		return query.getResultList();
	}
	
	private List<EndpointEntity> getByEndpointStatusAndTypes(List<String> statusNames, List<String> typeNames) {
		Query query = entityManager.createQuery("SELECT DISTINCT endpoint "
				+ "FROM EndpointEntity endpoint "
				+ "JOIN FETCH endpoint.endpointStatus "
				+ "JOIN FETCH endpoint.endpointType "
				+ "LEFT OUTER JOIN FETCH endpoint.mimeTypes "
				+ "LEFT OUTER JOIN FETCH endpoint.locationEndpointMaps locMaps "
				+ "LEFT OUTER JOIN FETCH locMaps.location "
				+ "WHERE UPPER(endpoint.endpointType.code) IN (:typeNames) "
				+ "AND UPPER(endpoint.endpointStatus.name) IN (:statusNames)", EndpointEntity.class);
		query.setParameter("typeNames", typeNames);
		query.setParameter("statusNames", statusNames);
		
		return query.getResultList();
	}
	
	@Override
	public List<EndpointEntity> getAllEntities() {
		Query query = entityManager.createQuery("SELECT DISTINCT endpoint "
				+ "FROM EndpointEntity endpoint "
				+ "JOIN FETCH endpoint.endpointStatus "
				+ "JOIN FETCH endpoint.endpointType "
				+ "LEFT OUTER JOIN FETCH endpoint.locationEndpointMaps locMaps "
				+ "LEFT OUTER JOIN FETCH locMaps.location "
				+ "LEFT OUTER JOIN FETCH endpoint.mimeTypes ", EndpointEntity.class);
		return query.getResultList();
	}
	
	private EndpointEntity convert(EndpointDTO endpointDto, EndpointEntity entity) {
		EndpointEntity endpointEntity = (entity != null ? entity : new EndpointEntity());
		endpointEntity.setAdapter(endpointDto.getAdapter());
		if(endpointDto.getEndpointStatus() != null) {
			if(endpointDto.getEndpointStatus().getId() != null) {
				endpointEntity.setEndpointStatusId(endpointDto.getEndpointStatus().getId());
			} else if(!StringUtils.isEmpty(endpointDto.getEndpointStatus().getName())) {
				EndpointStatusEntity statusEntity = getEndpointStatusByName(endpointDto.getEndpointStatus().getName());
				if(statusEntity != null) {
					endpointEntity.setEndpointStatusId(statusEntity.getId());
					endpointEntity.setEndpointStatus(statusEntity);
				} else {
					logger.error("Could not find endpoint status with name " + endpointDto.getEndpointStatus().getName());
				}
			}
		}
		if(endpointDto.getEndpointType() != null) {
			if(endpointDto.getEndpointType().getId() != null) {
				endpointEntity.setEndpointTypeId(endpointDto.getEndpointType().getId());
			} else if(!StringUtils.isEmpty(endpointDto.getEndpointType().getCode())) {
				EndpointTypeEntity typeEntity = getEndpointTypeByCode(endpointDto.getEndpointType().getCode());
				if(typeEntity != null) {
					endpointEntity.setEndpointTypeId(typeEntity.getId());
					endpointEntity.setEndpointType(typeEntity);
				} else {
					logger.error("Could not find endpoint type with code " + endpointDto.getEndpointType().getCode());
				}
			}
		}
		endpointEntity.setExternalId(endpointDto.getExternalId());
		endpointEntity.setExternalLastUpdateDate(endpointDto.getExternalLastUpdateDate());
		if(endpointDto.getMimeTypes() != null && endpointDto.getMimeTypes().size() > 0) {
			for(EndpointMimeTypeDTO mimetypeDto : endpointDto.getMimeTypes()) {
				if(endpointDto.getId() != null) {
					EndpointMimeTypeEntity mtEntity = 
							getEndpointMimeType(endpointDto.getId(), mimetypeDto.getMimeType());
					if(mtEntity == null) {
						mtEntity = new EndpointMimeTypeEntity();
						mtEntity.setMimeType(mimetypeDto.getMimeType());
						endpointEntity.getMimeTypes().add(mtEntity);
					} else {
						endpointEntity.getMimeTypes().add(mtEntity);
					}
				} else {
					EndpointMimeTypeEntity mtEntity = new EndpointMimeTypeEntity();
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
