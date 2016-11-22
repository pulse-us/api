package gov.ca.emsa.pulse.broker.dao.impl;

import gov.ca.emsa.pulse.broker.dao.OrganizationDAO;
import gov.ca.emsa.pulse.broker.dto.OrganizationDTO;
import gov.ca.emsa.pulse.broker.entity.LocationEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.persistence.EntityExistsException;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

@Repository
public class OrganizationDAOImpl extends BaseDAOImpl implements OrganizationDAO {

	public OrganizationDTO create(OrganizationDTO dto) throws EntityExistsException{

		LocationEntity entity = null;
		if(getByOrgId(dto.getOrganizationId()) != null){
			throw new EntityExistsException();
		} else {
			entity = new LocationEntity();
			entity.setExternalId(dto.getOrganizationId());
			entity.setName(dto.getName());
			entity.setActive(dto.isActive());
			entity.setAdapter(dto.getAdapter());
			if(dto.getIpAddress() != null){
				entity.setIpAddress(dto.getIpAddress());
			}
			if(dto.getUsername() != null){
				entity.setUsername(dto.getUsername());
			}
			if(dto.getPassword() != null){
				entity.setPassword(dto.getPassword());
			}
			if(dto.getCertificationKey() != null){
				entity.setCertificationKey(dto.getCertificationKey());
			}
			if(dto.getEndpointUrl() != null){
				entity.setEndpointUrl(dto.getEndpointUrl());
			}
			entity.setCreationDate(new Date());
			entity.setLastModifiedDate(new Date());
	
			create(entity);
		}
		return new OrganizationDTO(entity);
	}

	public OrganizationDTO update(OrganizationDTO dto){

		LocationEntity entity =  getByOrgId(dto.getOrganizationId());

		boolean changed = false;

		if(!entity.getName().equals(dto.getName())){
			entity.setName(dto.getName());
			changed = true;
		}
		if(entity.isActive() != dto.isActive()){
			entity.setActive(dto.isActive());
			changed = true;
		}
		if(!entity.getAdapter().equals(dto.getAdapter())){
			entity.setAdapter(dto.getAdapter());
			changed = true;
		}
		if(!Objects.equals(entity.getIpAddress(),dto.getIpAddress())){
			entity.setIpAddress(dto.getIpAddress());
			changed = true;
		}
		if(!Objects.equals(entity.getUsername(), dto.getUsername())){
			entity.setUsername(dto.getUsername());
			changed = true;
		}
		if(!Objects.equals(entity.getPassword(), dto.getPassword())){
			entity.setPassword(dto.getPassword());
			changed = true;
		}
		if(!Objects.equals(entity.getCertificationKey(), dto.getCertificationKey())){
			entity.setCertificationKey(dto.getCertificationKey());
			changed = true;
		}
		if(!Objects.equals(entity.getEndpointUrl(), dto.getEndpointUrl())){
			entity.setEndpointUrl(dto.getEndpointUrl());
			changed = true;
		}

		if(changed){
			entity.setLastModifiedDate(new Date());
			update(entity);
			OrganizationDTO result = null;
			if (entity != null){
				result = new OrganizationDTO(entity);
			}

			return result;
		}else{
			return dto;
		}

	}

	public void delete(OrganizationDTO organizationDTO) {
		LocationEntity toDelete = getOrganizationById(organizationDTO.getId());
		deleteOrganization(toDelete);
	}

	@Override
	public List<OrganizationDTO> findAll() {

		List<LocationEntity> entities = getAllEntities();
		List<OrganizationDTO> dtos = new ArrayList<>();

		for (LocationEntity entity : entities) {
			OrganizationDTO dto = new OrganizationDTO(entity);
			dtos.add(dto);
		}
		return dtos;
	}

	@Override
	public List<OrganizationDTO> findByName(String name) {

		Query query = entityManager.createQuery( "SELECT org from OrganizationEntity org "
				+ "where org.name LIKE :name", 
				LocationEntity.class );

		query.setParameter("name", name);
		List<LocationEntity> entities = query.getResultList();

		List<OrganizationDTO> dtos = new ArrayList<>();
		for (LocationEntity entity : entities) {
			OrganizationDTO dto = new OrganizationDTO(entity);
			dtos.add(dto);
		}
		return dtos;
	}

	@Override
	public OrganizationDTO findById(Long id) {
		LocationEntity org = getOrganizationById(id);
		if(org == null) {
			return null;
		}
		return new OrganizationDTO(org);
	}

	private void create(LocationEntity entity) {

		entityManager.persist(entity);
		entityManager.flush();

	}

	private void update(LocationEntity entity) {

		entityManager.merge(entity);
		entityManager.flush();

	}

	private LocationEntity getOrganizationById(Long id) {
		LocationEntity org = entityManager.find(LocationEntity.class, id);
		return org;
	}

	private LocationEntity getByOrgId(Long orgId) {
		Query query = entityManager.createQuery("from OrganizationEntity o "
				+ "WHERE o.organizationId = :orgId", LocationEntity.class);
		query.setParameter("orgId", orgId);
		
		List<LocationEntity> result = query.getResultList();
		if(result != null && result.size() > 0) {
			return result.get(0);
		}
		return null;
	}
	
	@Override
	public List<LocationEntity> getAllEntities() {
		Query query = entityManager.createQuery("from OrganizationEntity", LocationEntity.class);
		return query.getResultList();
	}

	public void deleteOrganization(LocationEntity org){
		entityManager.remove(org);
		entityManager.flush();
	}

}
