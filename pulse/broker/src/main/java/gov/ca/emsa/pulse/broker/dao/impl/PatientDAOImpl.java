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

import gov.ca.emsa.pulse.broker.dao.PatientDAO;
import gov.ca.emsa.pulse.broker.dao.QueryStatusDAO;
import gov.ca.emsa.pulse.broker.dto.PatientDTO;
import gov.ca.emsa.pulse.broker.dto.PatientEndpointMapDTO;
import gov.ca.emsa.pulse.broker.entity.AlternateCareFacilityEntity;
import gov.ca.emsa.pulse.broker.entity.PatientEntity;
import gov.ca.emsa.pulse.broker.entity.PatientEndpointMapEntity;
import gov.ca.emsa.pulse.common.domain.QueryEndpointStatus;

@Repository
public class PatientDAOImpl extends BaseDAOImpl implements PatientDAO {
	private static final Logger logger = LogManager.getLogger(PatientDAOImpl.class);
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
	public PatientEndpointMapDTO createPatientEndpointMap(PatientEndpointMapDTO toCreate) throws SQLException {
		PatientEndpointMapEntity orgMap = new PatientEndpointMapEntity();
		orgMap.setDocumentsQueryStatusId(statusDao.getQueryEndpointStatusByName(QueryEndpointStatus.Active.name()).getId());
		orgMap.setDocumentsQueryStart(new Date());
		orgMap.setDocumentsQueryEnd(null);
		orgMap.setEndpointId(toCreate.getEndpointId());
		orgMap.setExternalPatientRecordId(toCreate.getExternalPatientRecordId());
		orgMap.setPatientId(toCreate.getPatientId());

		entityManager.persist(orgMap);
		entityManager.flush();
		return new PatientEndpointMapDTO(orgMap);
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
	public PatientEndpointMapDTO updatePatientEndpointMap(PatientEndpointMapDTO toUpdate) throws SQLException {
		logger.debug("Looking up patient org map with id " + toUpdate.getId());
		PatientEndpointMapEntity orgMap = getOrgMapById(toUpdate.getId());
		if(orgMap == null) {
			logger.error("Could not find patient org map with id " + toUpdate.getId());
		}		
		orgMap.setDocumentsQueryStatusId(statusDao.getQueryEndpointStatusByName(toUpdate.getDocumentsQueryStatus().name()).getId());
		orgMap.setDocumentsQueryStart(toUpdate.getDocumentsQueryStart());
		orgMap.setDocumentsQueryEnd(toUpdate.getDocumentsQueryEnd());
		orgMap.setEndpointId(toUpdate.getEndpointId());
		orgMap.setExternalPatientRecordId(toUpdate.getExternalPatientRecordId());
		orgMap.setPatientId(toUpdate.getPatientId());

		entityManager.merge(orgMap);
		entityManager.flush();
		return new PatientEndpointMapDTO(orgMap);
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
	public PatientEndpointMapDTO getPatientEndpointMapById(Long id) {
		PatientEndpointMapEntity entity = null;

		Query query = entityManager.createQuery( "SELECT pat from PatientEndpointMapEntity pat "
				+ "LEFT OUTER JOIN FETCH pat.patient "
				+ "LEFT OUTER JOIN FETCH pat.endpoint "
				+ "LEFT OUTER JOIN FETCH pat.status "
				+ "where pat.id = :entityid) ", 
				PatientEndpointMapEntity.class );

		query.setParameter("entityid", id);
		List<PatientEndpointMapEntity> result = query.getResultList();
		if(result.size() == 1) {
			entity = result.get(0);
		}

		return new PatientEndpointMapDTO(entity);
	}

	@Override
	public List<PatientDTO> getPatientsAtAcf(Long acfId) {
		Query query = entityManager.createQuery( "SELECT distinct pat from PatientEntity pat "
				+ "LEFT OUTER JOIN FETCH pat.acf "
				+ "LEFT OUTER JOIN FETCH pat.endpointMaps "
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
		logger.info("Deleted " + deletedCount + " patients from the cache with last read date older than " + oldestDate.toString());
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

	private PatientEndpointMapEntity getOrgMapById(Long id) {		
		Query query = entityManager.createQuery( "SELECT distinct pat from PatientEndpointMapEntity pat "
				+ "LEFT OUTER JOIN FETCH pat.endpoint "
				+ "LEFT OUTER JOIN FETCH pat.patient "
				+ "LEFT OUTER JOIN FETCH pat.documents " 
				+ "LEFT OUTER JOIN FETCH pat.status "
				+ "where pat.id = :entityid) ", 
				PatientEndpointMapEntity.class );

		query.setParameter("entityid", id);

		PatientEndpointMapEntity entity = null;
		List<PatientEndpointMapEntity> results = query.getResultList();
		if(results.size() != 0) {
			entity = results.get(0);
		}
		return entity;
	}
}