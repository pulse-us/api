package gov.ca.emsa.pulse.broker.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import gov.ca.emsa.pulse.broker.dao.AddressDAO;
import gov.ca.emsa.pulse.broker.dao.AlternateCareFacilityDAO;
import gov.ca.emsa.pulse.broker.dao.PatientDAO;
import gov.ca.emsa.pulse.broker.dto.AddressDTO;
import gov.ca.emsa.pulse.broker.dto.AlternateCareFacilityDTO;
import gov.ca.emsa.pulse.broker.dto.PatientDTO;
import gov.ca.emsa.pulse.broker.entity.AddressEntity;
import gov.ca.emsa.pulse.broker.entity.AlternateCareFacilityEntity;

@Repository
public class AlternateCareFacilityDAOImpl extends BaseDAOImpl implements AlternateCareFacilityDAO {
	private static final Logger logger = LogManager.getLogger(AlternateCareFacilityDAOImpl.class);

	@Autowired PatientDAO patientDao;
	@Autowired AddressDAO addrDao;
	
	@Override
	public AlternateCareFacilityDTO create(AlternateCareFacilityDTO dto) {
		AlternateCareFacilityEntity toInsert = new AlternateCareFacilityEntity();
		toInsert.setName(dto.getName());
		toInsert.setPhoneNumber(dto.getPhoneNumber());
		toInsert.setLastReadDate(new Date());
		if(dto.getAddress() != null) {
			if(dto.getAddress().getId() == null) {
				AddressDTO addrDto = addrDao.create(dto.getAddress());
				dto.setAddress(addrDto);
			} 
			//address entity should exist now
			AddressEntity addr = entityManager.find(AddressEntity.class, dto.getAddress().getId());
			toInsert.setAddress(addr);
		}
		
		entityManager.persist(toInsert);
		entityManager.flush();
		return new AlternateCareFacilityDTO(toInsert);
	}

	@Override
	public AlternateCareFacilityDTO update(AlternateCareFacilityDTO dto) {
		AlternateCareFacilityEntity toUpdate = this.getEntityById(dto.getId());
		toUpdate.setName(dto.getName());
		toUpdate.setPhoneNumber(dto.getPhoneNumber());
		toUpdate.setLastReadDate(dto.getLastReadDate());
		if(dto.getAddress() != null) {
			if(dto.getAddress().getId() == null) {
				AddressDTO addrDto = addrDao.create(dto.getAddress());
				dto.setAddress(addrDto);
			} else {
				addrDao.update(dto.getAddress());
			}
			//address entity should exist now
			AddressEntity addr = entityManager.find(AddressEntity.class, dto.getAddress().getId());
			toUpdate.setAddress(addr);
		} else {
			toUpdate.setAddress(null);
		}
		
		toUpdate = entityManager.merge(toUpdate);
		return new AlternateCareFacilityDTO(toUpdate);
	}

	@Override
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
	
	@Override
	public void deleteItemsOlderThan(Date oldestItem) {
		Query query = entityManager.createQuery( "from AlternateCareFacilityEntity acf "
				+ "LEFT OUTER JOIN FETCH acf.address "
				+ " WHERE acf.lastReadDate <= :cacheDate");
		
		query.setParameter("cacheDate", oldestItem);
		List<AlternateCareFacilityEntity> oldAcfs = query.getResultList();
		if(oldAcfs != null && oldAcfs.size() > 0) {
			for(AlternateCareFacilityEntity oldAcf : oldAcfs) {
				entityManager.remove(oldAcf);
				logger.info("Deleted ACF with ID " + oldAcf.getId() + " and name " + oldAcf.getName() + " during ACF cleanup.");
			}
		} else {
			logger.info("Deleted 0 ACFs from the cache.");
		}
	}
	
	private List<AlternateCareFacilityEntity> findAllEntities() {
		Query query = entityManager.createQuery("SELECT a from AlternateCareFacilityEntity a "
				+ "LEFT OUTER JOIN FETCH a.address");
		return query.getResultList();
	}
	
	private AlternateCareFacilityEntity getEntityById(Long id) {
		AlternateCareFacilityEntity entity = null;
		
		Query query = entityManager.createQuery( "SELECT a from AlternateCareFacilityEntity a "
				+ "LEFT OUTER JOIN FETCH a.address "
				+ "where a.id = :entityid", AlternateCareFacilityEntity.class );
		query.setParameter("entityid", id);
		
		List<AlternateCareFacilityEntity> result = query.getResultList();
		if(result.size() != 0) {
			entity = result.get(0);
		}
		
		return entity;
	}
	
	private List<AlternateCareFacilityEntity> getEntityByName(String name) {
		AlternateCareFacilityEntity entity = null;
		
		Query query = entityManager.createQuery( "from AlternateCareFacilityEntity a "
				+ "LEFT OUTER JOIN FETCH a.address "
				+ "where (a.name LIKE :name) ", AlternateCareFacilityEntity.class );
		query.setParameter("name", name);
		return query.getResultList();
	}
}
