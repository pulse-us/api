package gov.ca.emsa.pulse.broker.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import gov.ca.emsa.pulse.broker.dao.PatientDAO;
import gov.ca.emsa.pulse.broker.dto.PatientDTO;
import gov.ca.emsa.pulse.broker.entity.PatientEntity;

@Repository
public class PatientDAOImpl extends BaseDAOImpl implements PatientDAO {
	private static final Logger logger = LogManager.getLogger(PatientDAOImpl.class);
	
	@Override
	public PatientDTO create(PatientDTO dto) {
		PatientEntity patient = new PatientEntity();
		patient.setFirstName(dto.getFirstName());
		patient.setLastName(dto.getLastName());
		patient.setDateOfBirth(dto.getDateOfBirth());
		patient.setSsn(dto.getSsn());
		patient.setGender(dto.getGender());
		patient.setPhoneNumber(dto.getPhoneNumber());
		patient.setAcfId(dto.getAcf().getId());
		patient.setLastReadDate(new Date());
		if(dto.getAddress() != null) {
			patient.setAddressId(dto.getAddress().getId());
		}
		
		entityManager.persist(patient);
		entityManager.flush();
		return new PatientDTO(patient);
	}

	@Override
	public PatientDTO update(PatientDTO dto) {
		PatientEntity patient = this.getEntityById(dto.getId());
		patient.setFirstName(dto.getFirstName());
		patient.setLastName(dto.getLastName());
		patient.setDateOfBirth(dto.getDateOfBirth());
		patient.setSsn(dto.getSsn());
		patient.setGender(dto.getGender());
		patient.setPhoneNumber(dto.getPhoneNumber());
		patient.setAcfId(dto.getAcf().getId());
		patient.setLastReadDate(dto.getLastReadDate());
		if(dto.getAddress() != null) {
			patient.setAddressId(dto.getAddress().getId());
		} else {
			patient.setAddressId(null);
		}
		
		patient = entityManager.merge(patient);
		entityManager.flush();
		return new PatientDTO(patient);
	}

	@Override
	public void delete(Long id) {
		PatientEntity toDelete = getEntityById(id);
		entityManager.remove(toDelete);
		entityManager.flush();
	}

	
	@Override
	public PatientDTO getById(Long id) {
		PatientDTO dto = null;
		PatientEntity pe = this.getEntityById(id);
		
		if (pe != null){
			dto = new PatientDTO(pe); 
		}
		return dto;
	}
	
	@Override
	public List<PatientDTO> getPatientsAtAcf(Long acfId) {
		Query query = entityManager.createQuery( "SELECT pat from PatientEntity pat "
				+ "LEFT OUTER JOIN FETCH pat.acf "
				+ "LEFT OUTER JOIN FETCH pat.address "
				+ "LEFT OUTER JOIN FETCH pat.orgMaps "
				+ "where pat.acfId = :acfId) ", 
				PatientEntity.class );
		
		query.setParameter("entityid", acfId);
		List<PatientEntity> patients = query.getResultList();
		List<PatientDTO> results = new ArrayList<PatientDTO>();
		for(PatientEntity patient : patients) {
			results.add(new PatientDTO(patient));
		}
		return results;
	}
	
	@Override
	public void deleteItemsOlderThan(Date oldestDate) {			
		Query query = entityManager.createQuery( "DELETE from PatientEntity pat "
				+ " WHERE pat.lastReadDate <= :cacheDate");
		
		query.setParameter("cacheDate", oldestDate);
		int deletedCount = query.executeUpdate();		
		logger.info("Deleted " + deletedCount + " patients from the cache.");
	}

	private PatientEntity getEntityById(Long id) {
		PatientEntity entity = null;
		
		Query query = entityManager.createQuery( "SELECT pat from PatientEntity pat "
				+ "LEFT OUTER JOIN FETCH pat.acf "
				+ "LEFT OUTER JOIN FETCH pat.address "
				+ "LEFT OUTER JOIN FETCH pat.orgMaps "
				+ "where pat.id = :entityid) ", 
				PatientEntity.class );
		
		query.setParameter("entityid", id);
		List<PatientEntity> result = query.getResultList();
		if(result.size() == 1) {
			entity = result.get(0);
		}
		
		return entity;
	}
}