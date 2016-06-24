package gov.ca.emsa.pulse.broker.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import gov.ca.emsa.pulse.broker.dao.AlternateCareFacilityDAO;
import gov.ca.emsa.pulse.broker.dto.AlternateCareFacilityDTO;
import gov.ca.emsa.pulse.broker.entity.AlternateCareFacilityEntity;

@Repository
public class AlternateCareFacilityDAOImpl extends BaseDAOImpl implements AlternateCareFacilityDAO {

	@Override
	public AlternateCareFacilityDTO create(AlternateCareFacilityDTO dto) {
		AlternateCareFacilityEntity toInsert = new AlternateCareFacilityEntity();
		toInsert.setName(dto.getName());
		toInsert.setLastReadDate(new Date());
		
		entityManager.persist(toInsert);
		entityManager.flush();
		return new AlternateCareFacilityDTO(toInsert);
	}

	@Override
	@Transactional
	public AlternateCareFacilityDTO update(AlternateCareFacilityDTO dto) {
		AlternateCareFacilityEntity entity = this.getEntityById(dto.getId());
		entity.setName(dto.getName());
		entity.setLastReadDate(dto.getLastReadDate());
		
		entity = entityManager.merge(entity);
		return new AlternateCareFacilityDTO(entity);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		AlternateCareFacilityEntity toDelete = getEntityById(id);
		entityManager.remove(toDelete);
	}

	@Override
	public List<AlternateCareFacilityDTO> findAll() {
		List<AlternateCareFacilityEntity> result = this.findAllEntities();
		List<AlternateCareFacilityDTO> dtos = new ArrayList<AlternateCareFacilityDTO>(result.size());
		for(AlternateCareFacilityEntity entity : result) {
			dtos.add(new AlternateCareFacilityDTO(entity));
		}
		return dtos;
	}
	
	@Override
	public AlternateCareFacilityDTO getById(Long id) {
		
		AlternateCareFacilityDTO dto = null;
		AlternateCareFacilityEntity ae = this.getEntityById(id);
		
		if (ae != null){
			dto = new AlternateCareFacilityDTO(ae); 
		}
		return dto;
	}

	@Override
	public List<AlternateCareFacilityDTO> getByName(String name) {
		List<AlternateCareFacilityEntity> results = this.getEntityByName(name);
		List<AlternateCareFacilityDTO> dtos = new ArrayList<AlternateCareFacilityDTO>();
		for(AlternateCareFacilityEntity entity : results) {
			AlternateCareFacilityDTO dto = new AlternateCareFacilityDTO(entity);
			dtos.add(dto);
		}
		return dtos;
	}
	
	private List<AlternateCareFacilityEntity> findAllEntities() {
		Query query = entityManager.createQuery("SELECT a from AlternateCareFacilityEntity");
		return query.getResultList();
	}
	
	private AlternateCareFacilityEntity getEntityById(Long id) {
		AlternateCareFacilityEntity entity = null;
		
		Query query = entityManager.createQuery( "from AlternateCareFacilityEntity a where (id = :entityid) ", AlternateCareFacilityEntity.class );
		query.setParameter("entityid", id);
		List<AlternateCareFacilityEntity> result = query.getResultList();
		
		if(result.size() == 1) {
			entity = result.get(0);
		}
		
		return entity;
	}
	
	private List<AlternateCareFacilityEntity> getEntityByName(String name) {
		AlternateCareFacilityEntity entity = null;
		
		Query query = entityManager.createQuery( "from AlternateCareFacilityEntity a where (name LIKE :name) ", AlternateCareFacilityEntity.class );
		query.setParameter("name", name);
		return query.getResultList();
	}
}
