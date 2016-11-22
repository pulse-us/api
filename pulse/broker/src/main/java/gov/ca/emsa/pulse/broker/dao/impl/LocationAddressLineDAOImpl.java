package gov.ca.emsa.pulse.broker.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import gov.ca.emsa.pulse.broker.dao.LocationAddressLineDAO;
import gov.ca.emsa.pulse.broker.dto.AddressLineDTO;
import gov.ca.emsa.pulse.broker.entity.LocationAddressLineEntity;

@Repository
public class LocationAddressLineDAOImpl extends BaseDAOImpl implements LocationAddressLineDAO {

	@Override
	public AddressLineDTO create(AddressLineDTO dto, Long locationId, Integer lineOrder) {
		LocationAddressLineEntity toInsert = new LocationAddressLineEntity();
		toInsert.setLocationId(locationId);
		toInsert.setOrder(lineOrder);
		toInsert.setLine(dto.getLine());	
		entityManager.persist(toInsert);
		entityManager.flush();

		return new AddressLineDTO(toInsert);
	}

	@Override
	public AddressLineDTO update(AddressLineDTO dto) {
		LocationAddressLineEntity toUpdate = this.queryEntityById(dto.getId());
		toUpdate.setLine(dto.getLine());
		LocationAddressLineEntity updated = entityManager.merge(toUpdate);
		entityManager.flush();
		return new AddressLineDTO(updated);
	}

	@Override
	public void delete(Long id) {
		LocationAddressLineEntity toDelete = queryEntityById(id);
		entityManager.remove(toDelete);
		entityManager.flush();
	}

	@Override
	public void deleteAllForLocation(Long locationId) {
		List<LocationAddressLineEntity> toDeleteEntities = queryAllForLocation(locationId);
		for(LocationAddressLineEntity toDelete : toDeleteEntities) {
			entityManager.remove(toDelete);
		}
		entityManager.flush();
	}
	
	@Override
	public List<AddressLineDTO> getByLocation(Long locationId) {
		List<LocationAddressLineEntity> entities = queryAllForLocation(locationId);
		List<AddressLineDTO> dtos = new ArrayList<AddressLineDTO>(entities.size());
		for(LocationAddressLineEntity entity : entities) {
			dtos.add(new AddressLineDTO(entity));
		}
		return dtos;
	}
	
	@Override
	public AddressLineDTO getByLocationAndLine(Long locationId, String line) {
		
		AddressLineDTO dto = null;
		LocationAddressLineEntity ae = queryByLocationAndLine(locationId, line);
		
		if (ae != null){
			dto = new AddressLineDTO(ae); 
		}
		return dto;
	}
	
	private LocationAddressLineEntity queryEntityById(Long id) {
		LocationAddressLineEntity entity = null;
		
		Query query = entityManager.createQuery( "from LocationAddressLineEntity a "
				+ "where (id = :entityid) ", LocationAddressLineEntity.class );
		query.setParameter("entityid", id);
		List<LocationAddressLineEntity> result = query.getResultList();
		
		if(result.size() == 1) {
			entity = result.get(0);
		}
		return entity;
	}
	
	private List<LocationAddressLineEntity> queryAllForLocation(Long locationId) {
		List<LocationAddressLineEntity> result = new ArrayList<LocationAddressLineEntity>();
		
		//get the first address line
		Query query = entityManager.createQuery( "from LocationAddressLineEntity a "
				+ "where (locationId = :v)", 
				LocationAddressLineEntity.class );
		query.setParameter("locationId", locationId);
		return query.getResultList();
	}
	
	private LocationAddressLineEntity queryByLocationAndLine(Long locationId, String line) {		
		LocationAddressLineEntity entity = null;

		Query query = entityManager.createQuery( "from LocationAddressLineEntity a "
				+ "where (locationId = :locationId) "
				+ "and (UPPER(line) LIKE :line)", LocationAddressLineEntity.class );
		query.setParameter("locationId", locationId);
		query.setParameter("line", line.toUpperCase());
		
		List<LocationAddressLineEntity> result = query.getResultList();
		if(result.size() == 1) {
			entity = result.get(0);
		}
		return entity;
	}
}
