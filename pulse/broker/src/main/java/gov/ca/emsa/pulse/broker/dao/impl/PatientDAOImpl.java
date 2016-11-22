package gov.ca.emsa.pulse.broker.dao.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import gov.ca.emsa.pulse.broker.cache.CacheCleanupException;
import gov.ca.emsa.pulse.broker.dao.AddressDAO;
import gov.ca.emsa.pulse.broker.dao.PatientDAO;
import gov.ca.emsa.pulse.broker.dao.PatientRecordNameDAO;
import gov.ca.emsa.pulse.broker.dao.QueryStatusDAO;
import gov.ca.emsa.pulse.broker.dto.AddressDTO;
import gov.ca.emsa.pulse.broker.dto.PatientDTO;
import gov.ca.emsa.pulse.broker.dto.PatientOrganizationMapDTO;
import gov.ca.emsa.pulse.broker.entity.AddressEntity;
import gov.ca.emsa.pulse.broker.entity.AlternateCareFacilityEntity;
import gov.ca.emsa.pulse.broker.entity.PatientEntity;
import gov.ca.emsa.pulse.broker.entity.PatientLocationMapEntity;
import gov.ca.emsa.pulse.common.domain.QueryLocationStatus;

@Repository
public class PatientDAOImpl extends BaseDAOImpl implements PatientDAO {
	private static final Logger logger = LogManager.getLogger(PatientDAOImpl.class);
	@Autowired AddressDAO addrDao;
	@Autowired QueryStatusDAO statusDao;

	@Override
	public PatientDTO create(PatientDTO dto) throws SQLException {
		PatientEntity patient = new PatientEntity();
		patient.setFullName(dto.getFullName());
		patient.setFriendlyName(dto.getFriendlyName());
		patient.setDateOfBirth(dto.getDateOfBirth());
		patient.setSsn(dto.getSsn());
		patient.setGender(dto.getGender());
		if(dto.getAcf() != null) {
			patient.setAcfId(dto.getAcf().getId());
			AlternateCareFacilityEntity acf = entityManager.find(AlternateCareFacilityEntity.class, dto.getAcf().getId());
			patient.setAcf(acf);
		}
		patient.setLastReadDate(new Date());

		entityManager.persist(patient);
		entityManager.flush();
		return new PatientDTO(patient);
	}

	@Override
	public PatientOrganizationMapDTO createOrgMap(PatientOrganizationMapDTO toCreate) throws SQLException {
		PatientLocationMapEntity orgMap = new PatientLocationMapEntity();
		orgMap.setDocumentsQueryStatusId(statusDao.getStatusByName(QueryLocationStatus.Active.name()).getId());
		orgMap.setDocumentsQueryStart(new Date());
		orgMap.setDocumentsQueryEnd(null);
		orgMap.setLocationId(toCreate.getOrganizationId());
		orgMap.setExternalPatientRecordId(toCreate.getOrgPatientRecordId());
		orgMap.setPatientId(toCreate.getPatientId());

		entityManager.persist(orgMap);
		entityManager.flush();
		return new PatientOrganizationMapDTO(orgMap);
	}

	@Override
	public PatientDTO update(PatientDTO dto) throws SQLException {
		PatientEntity patient = this.getEntityById(dto.getId());
		patient.setFriendlyName(dto.getFriendlyName());
		patient.setFullName(dto.getFullName());
		patient.setDateOfBirth(dto.getDateOfBirth());
		patient.setSsn(dto.getSsn());
		patient.setGender(dto.getGender());
		if(dto.getAcf() != null) {
			patient.setAcfId(dto.getAcf().getId());
		} else {
			patient.setAcfId(null);
		}
		patient.setLastReadDate(dto.getLastReadDate());
		patient = entityManager.merge(patient);
		entityManager.flush();
		return new PatientDTO(patient);
	}

	@Override
	public PatientOrganizationMapDTO updateOrgMap(PatientOrganizationMapDTO toUpdate) throws SQLException {
		logger.debug("Looking up patient org map with id " + toUpdate.getId());
		PatientLocationMapEntity orgMap = getOrgMapById(toUpdate.getId());
		if(orgMap == null) {
			logger.error("Could not find patient org map with id " + toUpdate.getId());
		}		
		orgMap.setDocumentsQueryStatusId(statusDao.getStatusByName(toUpdate.getDocumentsQueryStatus().name()).getId());
		orgMap.setDocumentsQueryStart(toUpdate.getDocumentsQueryStart());
		orgMap.setDocumentsQueryEnd(toUpdate.getDocumentsQueryEnd());
		orgMap.setLocationId(toUpdate.getOrganizationId());
		orgMap.setExternalPatientRecordId(toUpdate.getOrgPatientRecordId());
		orgMap.setPatientId(toUpdate.getPatientId());

		entityManager.merge(orgMap);
		entityManager.flush();
		return new PatientOrganizationMapDTO(orgMap);
	}

	@Override
	public void delete(Long id) throws SQLException {
		PatientEntity toDelete = getEntityById(id);
		if(toDelete != null) {
			try {
				entityManager.remove(toDelete);
				entityManager.flush();
			} catch(Exception ex) {
				logger.error("Could not delete patient. Was it removed before the delete method was called?", ex);
			}
		}
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
	public PatientOrganizationMapDTO getPatientOrgMapById(Long id) {
		PatientLocationMapEntity entity = null;

		Query query = entityManager.createQuery( "SELECT pat from PatientOrganizationMapEntity pat "
				+ "LEFT OUTER JOIN FETCH pat.patient "
				+ "LEFT OUTER JOIN FETCH pat.organization "
				+ "LEFT OUTER JOIN FETCH pat.status "
				+ "where pat.id = :entityid) ", 
				PatientLocationMapEntity.class );

		query.setParameter("entityid", id);
		List<PatientLocationMapEntity> result = query.getResultList();
		if(result.size() == 1) {
			entity = result.get(0);
		}

		return new PatientOrganizationMapDTO(entity);
	}

	@Override
	public List<PatientDTO> getPatientsAtAcf(Long acfId) {
		Query query = entityManager.createQuery( "SELECT distinct pat from PatientEntity pat "
				+ "LEFT OUTER JOIN FETCH pat.acf "
				+ "LEFT OUTER JOIN FETCH pat.orgMaps "
				+ "where pat.acfId = :acfId) ", 
				PatientEntity.class );

		query.setParameter("acfId", acfId);
		List<PatientEntity> patients = query.getResultList();
		List<PatientDTO> results = new ArrayList<PatientDTO>();
		for(PatientEntity patient : patients) {
			results.add(new PatientDTO(patient));
		}
		return results;
	}

	@Override
	public void deleteItemsOlderThan(Date oldestDate) throws SQLException {			
		Query query = entityManager.createQuery( "DELETE from PatientEntity pat "
				+ " WHERE pat.lastReadDate <= :cacheDate");

		query.setParameter("cacheDate", oldestDate);
		int deletedCount = query.executeUpdate();		
		logger.info("Deleted " + deletedCount + " patients from the cache.");
	}

	private PatientEntity getEntityById(Long id) {
		PatientEntity entity = null;

		Query query = entityManager.createQuery( "SELECT distinct pat from PatientEntity pat "
				+ "LEFT OUTER JOIN FETCH pat.acf "
				+ "where pat.id = :entityid) ", 
				PatientEntity.class );

		query.setParameter("entityid", id);
		List<PatientEntity> patients = query.getResultList();
		if(patients.size() != 0) {
			entity = patients.get(0);
		}
		return entity;
	}

	private PatientLocationMapEntity getOrgMapById(Long id) {		
		Query query = entityManager.createQuery( "SELECT distinct pat from PatientOrganizationMapEntity pat "
				+ "LEFT OUTER JOIN FETCH pat.organization "
				+ "LEFT OUTER JOIN FETCH pat.patient "
				+ "LEFT OUTER JOIN FETCH pat.documents " 
				+ "LEFT OUTER JOIN FETCH pat.status "
				+ "where pat.id = :entityid) ", 
				PatientLocationMapEntity.class );

		query.setParameter("entityid", id);

		PatientLocationMapEntity entity = null;
		List<PatientLocationMapEntity> orgMaps = query.getResultList();
		if(orgMaps.size() != 0) {
			entity = orgMaps.get(0);
		}
		return entity;
	}
}