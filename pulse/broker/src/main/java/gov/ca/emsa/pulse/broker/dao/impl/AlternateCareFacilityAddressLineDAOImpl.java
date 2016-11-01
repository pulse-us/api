package gov.ca.emsa.pulse.broker.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import gov.ca.emsa.pulse.broker.dao.AlternateCareFacilityAddressLineDAO;
import gov.ca.emsa.pulse.broker.dto.AddressLineDTO;
import gov.ca.emsa.pulse.broker.entity.AlternateCareFacilityAddressLineEntity;

@Repository
public class AlternateCareFacilityAddressLineDAOImpl extends BaseDAOImpl implements AlternateCareFacilityAddressLineDAO {

	@Override
	public AddressLineDTO create(AddressLineDTO dto, Long acfId, Integer lineOrder) {
		AlternateCareFacilityAddressLineEntity toInsert = new AlternateCareFacilityAddressLineEntity();
		toInsert.setAcfId(acfId);
		toInsert.setOrder(lineOrder);
		toInsert.setLine(dto.getLine());	
		entityManager.persist(toInsert);
		entityManager.flush();

		return new AddressLineDTO(toInsert);
	}

	@Override
	public AddressLineDTO update(AddressLineDTO dto) {
		AlternateCareFacilityAddressLineEntity toUpdate = this.queryEntityById(dto.getId());
		toUpdate.setLine(dto.getLine());
		AlternateCareFacilityAddressLineEntity updated = entityManager.merge(toUpdate);
		entityManager.flush();
		return new AddressLineDTO(updated);
	}

	@Override
	public void delete(Long id) {
		AlternateCareFacilityAddressLineEntity toDelete = queryEntityById(id);
		entityManager.remove(toDelete);
		entityManager.flush();
	}

	@Override
	public void deleteAllForAcf(Long acfId) {
		List<AlternateCareFacilityAddressLineEntity> toDeleteEntities = queryAllForAcf(acfId);
		for(AlternateCareFacilityAddressLineEntity toDelete : toDeleteEntities) {
			entityManager.remove(toDelete);
		}
		entityManager.flush();
	}
	
	@Override
	public List<AddressLineDTO> getByAcf(Long acfId) {
		List<AlternateCareFacilityAddressLineEntity> entities = queryAllForAcf(acfId);
		List<AddressLineDTO> dtos = new ArrayList<AddressLineDTO>(entities.size());
		for(AlternateCareFacilityAddressLineEntity entity : entities) {
			dtos.add(new AddressLineDTO(entity));
		}
		return dtos;
	}
	
	@Override
	public AddressLineDTO getByAcfAndLine(Long acfId, String line) {
		
		AddressLineDTO dto = null;
		AlternateCareFacilityAddressLineEntity ae = queryByAcfAndLine(acfId, line);
		
		if (ae != null){
			dto = new AddressLineDTO(ae); 
		}
		return dto;
	}
	
	private AlternateCareFacilityAddressLineEntity queryEntityById(Long id) {
		AlternateCareFacilityAddressLineEntity entity = null;
		
		Query query = entityManager.createQuery( "from AlternateCareFacilityAddressLineEntity a "
				+ "where (id = :entityid) ", AlternateCareFacilityAddressLineEntity.class );
		query.setParameter("entityid", id);
		List<AlternateCareFacilityAddressLineEntity> result = query.getResultList();
		
		if(result.size() == 1) {
			entity = result.get(0);
		}
		return entity;
	}
	
	private List<AlternateCareFacilityAddressLineEntity> queryAllForAcf(Long acfId) {
		List<AlternateCareFacilityAddressLineEntity> result = new ArrayList<AlternateCareFacilityAddressLineEntity>();
		
		//get the first address line
		Query query = entityManager.createQuery( "from AlternateCareFacilityAddressLineEntity a "
				+ "where (acfId = :acfId)", 
				AlternateCareFacilityAddressLineEntity.class );
		query.setParameter("acfId", acfId);
		return query.getResultList();
	}
	
	private AlternateCareFacilityAddressLineEntity queryByAcfAndLine(Long acfId, String line) {		
		AlternateCareFacilityAddressLineEntity entity = null;

		Query query = entityManager.createQuery( "from AlternateCareFacilityAddressLineEntity a "
				+ "where (acfId = :acfId) "
				+ "and (UPPER(line) LIKE :line)", AlternateCareFacilityAddressLineEntity.class );
		query.setParameter("acfId", acfId);
		query.setParameter("line", line.toUpperCase());
		
		List<AlternateCareFacilityAddressLineEntity> result = query.getResultList();
		if(result.size() == 1) {
			entity = result.get(0);
		}
		return entity;
	}
}
