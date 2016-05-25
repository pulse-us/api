package gov.ca.emsa.pulse.broker.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityExistsException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import gov.ca.emsa.pulse.broker.dao.OrganizationDAO;
import gov.ca.emsa.pulse.broker.dto.OrganizationDTO;
import gov.ca.emsa.pulse.broker.entity.OrganizationEntity;

@Repository
public class OrganizationDAOImpl extends BaseDAOImpl implements OrganizationDAO {
	
	public OrganizationDTO create(OrganizationDTO dto) throws EntityExistsException{

		OrganizationEntity entity = null;
		OrganizationDTO result = null;
		if(this.getOrganizationById(dto.getOrganizationId()) != null){
			throw new EntityExistsException();
		}else{

			entity = new OrganizationEntity();
			entity.setOrganizationId(dto.getOrganizationId());
			entity.setName(dto.getName());
			entity.setActive(dto.isActive());
			entity.setAdapter(dto.getAdapter());
			entity.setIpAddress(dto.getIpAddress());
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

			result = new OrganizationDTO(entity);
		}
		return result;
	}
	
	public OrganizationDTO update(OrganizationDTO dto){

		OrganizationEntity entity =  this.getOrganizationById(dto.getOrganizationId());

		boolean changed = false;

		if(!entity.getName().equals(dto.getName())){
			entity.setName(dto.getName());
			changed = true;
		}else if(!entity.isActive() != dto.isActive()){
			entity.setActive(dto.isActive());
			changed = true;
		}else if(!entity.getAdapter().equals(dto.getAdapter())){
			entity.setAdapter(dto.getAdapter());
			changed = true;
		}else if(!entity.getIpAddress().equals(dto.getIpAddress())){
			entity.setIpAddress(dto.getIpAddress());
			changed = true;
		}else if(entity.getUsername() == null ? dto.getUsername() == null :
							!entity.getUsername().equals(dto.getUsername())){
			entity.setUsername(dto.getUsername());
			changed = true;
		}else if(entity.getPassword() == null ? dto.getPassword() == null :
								!entity.getPassword().equals(dto.getPassword())){
			entity.setPassword(dto.getPassword());
			changed = true;
		}else if(entity.getCertificationKey() == null ? dto.getCertificationKey() == null :
				!entity.getCertificationKey().equals(dto.getCertificationKey())){
			entity.setCertificationKey(dto.getCertificationKey());
			changed = true;
		}else if(entity.getEndpointUrl() == null ? dto.getEndpointUrl() == null :
			!entity.getEndpointUrl().equals(dto.getEndpointUrl())){
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
		OrganizationEntity toDelete = getOrganizationById(organizationDTO.getOrganizationId());
		deleteOrganization(toDelete);
	}
	
	@Override
	public List<OrganizationDTO> findAll() {
		
		List<OrganizationEntity> entities = getAllEntities();
		List<OrganizationDTO> dtos = new ArrayList<>();
		
		for (OrganizationEntity entity : entities) {
			OrganizationDTO dto = new OrganizationDTO(entity);
			dtos.add(dto);
		}
		return dtos;
	}
	
	@Override
	public List<OrganizationDTO> findByName(String name) {
		
		Query query = entityManager.createQuery( "SELECT org from OrganizationEntity org "
				+ "where org.name LIKE :name", 
				OrganizationEntity.class );
		
		query.setParameter("name", name);
		List<OrganizationEntity> entities = query.getResultList();
		
		List<OrganizationDTO> dtos = new ArrayList<>();
		for (OrganizationEntity entity : entities) {
			OrganizationDTO dto = new OrganizationDTO(entity);
			dtos.add(dto);
		}
		return dtos;
	}
	
	@Override
	public OrganizationDTO findById(Long id) {
		OrganizationEntity org = getOrganizationById(id);
		if(org == null) {
			return null;
		}
		return new OrganizationDTO(org);
	}
	
	private void create(OrganizationEntity entity) {
		
		entityManager.persist(entity);
		entityManager.flush();
		
	}
	
	private void update(OrganizationEntity entity) {

		entityManager.merge(entity);
		entityManager.flush();

	}
	
		
	private OrganizationEntity getOrganizationById(Long id) {
		OrganizationEntity org = null;
		TypedQuery<OrganizationEntity> query = null;
		
		query = entityManager.createQuery("from OrganizationEntity where (organization_id = :id) ", OrganizationEntity.class);
		
		query.setParameter("id", id);
		
		List<OrganizationEntity> result = query.getResultList();
		
		if(result.size() == 0){
			return null;
		}else if(result.size() == 1){
			org = result.get(0);
		}else{
			return null;
		}
		return org;
	}
	
	@Override
	public List<OrganizationEntity> getAllEntities() {
		TypedQuery<OrganizationEntity> query = null;
		
		query = entityManager.createQuery("from OrganizationEntity", OrganizationEntity.class);
		
		List<OrganizationEntity> result = query.getResultList();
		
		return result;
	}

	public void deleteOrganization(OrganizationEntity org){
		entityManager.remove(org);
	}
	
}
