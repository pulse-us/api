package gov.ca.emsa.pulse.broker.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import gov.ca.emsa.pulse.broker.dao.AlternateCareFacilityDAO;
import gov.ca.emsa.pulse.broker.dao.PatientDAO;
import gov.ca.emsa.pulse.broker.dto.AlternateCareFacilityDTO;
import gov.ca.emsa.pulse.broker.dto.PatientDTO;
import gov.ca.emsa.pulse.broker.entity.AlternateCareFacilityEntity;

@Repository
public class AlternateCareFacilityDAOImpl extends BaseDAOImpl implements AlternateCareFacilityDAO {
	private static final Logger logger = LogManager.getLogger(AlternateCareFacilityDAOImpl.class);

	@Autowired PatientDAO patientDao;
	
	@Override
	public AlternateCareFacilityDTO create(AlternateCareFacilityDTO dto) {
		AlternateCareFacilityEntity toInsert = new AlternateCareFacilityEntity();
		toInsert.setName(dto.getName());
		toInsert.setPhoneNumber(dto.getPhoneNumber());
		toInsert.setLastReadDate(new Date());
		if(dto.getAddress() != null) {
			toInsert.setAddressId(dto.getAddress().getId());
		}
		
		entityManager.persist(toInsert);
		entityManager.flush();
		return getById(toInsert.getId());
	}

	@Override
	@Transactional
	public AlternateCareFacilityDTO update(AlternateCareFacilityDTO dto) {
		AlternateCareFacilityEntity entity = this.getEntityById(dto.getId());
		entity.setName(dto.getName());
		entity.setPhoneNumber(dto.getPhoneNumber());
		entity.setLastReadDate(dto.getLastReadDate());
		if(dto.getAddress() != null) {
			entity.setAddressId(dto.getAddress().getId());
		} else {
			entity.setAddressId(null);
		}
		
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
	
	@Override
	public void deleteItemsOlderThan(Date oldestItem) {
		Query query = entityManager.createQuery( "from AlternateCareFacilityEntity acf "
				+ " WHERE acf.lastReadDate <= :cacheDate");
		
		query.setParameter("cacheDate", oldestItem);
		List<AlternateCareFacilityDTO> oldAcfs = query.getResultList();
		if(oldAcfs != null && oldAcfs.size() > 0) {
			for(AlternateCareFacilityDTO oldAcf : oldAcfs) {
				List<PatientDTO> patientsAtAcf = patientDao.getPatientsAtAcf(oldAcf.getId());
				if(patientsAtAcf == null || patientsAtAcf.size() == 0) {
					delete(oldAcf.getId());
					logger.info("Deleted ACF with ID " + oldAcf.getId() + " and name " + oldAcf.getName() + " during ACF cleanup.");
				}
			}
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
				+ "where (a.id = :entityid) ", AlternateCareFacilityEntity.class );
		query.setParameter("entityid", id);
		List<AlternateCareFacilityEntity> result = query.getResultList();
		
		if(result.size() == 1) {
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
