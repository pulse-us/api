package gov.ca.emsa.pulse.broker.manager.impl;

import gov.ca.emsa.pulse.broker.cache.CacheCleanupException;
import gov.ca.emsa.pulse.broker.dao.LocationDAO;
import gov.ca.emsa.pulse.broker.dao.PatientDAO;
import gov.ca.emsa.pulse.broker.dao.QueryDAO;
import gov.ca.emsa.pulse.broker.dto.LocationDTO;
import gov.ca.emsa.pulse.broker.dto.PatientDTO;
import gov.ca.emsa.pulse.broker.dto.PatientLocationMapDTO;
import gov.ca.emsa.pulse.broker.dto.PatientRecordDTO;
import gov.ca.emsa.pulse.broker.dto.QueryLocationMapDTO;
import gov.ca.emsa.pulse.broker.manager.AlternateCareFacilityManager;
import gov.ca.emsa.pulse.broker.manager.PatientManager;
import gov.ca.emsa.pulse.broker.manager.QueryManager;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PatientManagerImpl implements PatientManager {
	@Autowired private PatientDAO patientDao;
	@Autowired private LocationDAO locationDao;
	@Autowired private QueryManager queryManager;
	@Autowired private AlternateCareFacilityManager acfManager;
	@Autowired private QueryDAO queryDao;
	
	public PatientManagerImpl() {
	}
	
	@Override
	@Transactional	
	public PatientDTO getPatientById(Long patientId) {
		PatientDTO result = patientDao.getById(patientId);
		return result;
	}
	
	@Override
	@Transactional	
	public List<PatientDTO> getPatientsAtAcf(Long acfId) {
		List<PatientDTO> results = patientDao.getPatientsAtAcf(acfId);
		return results;
	}
	
	@Override
	@Transactional
	public PatientDTO create(PatientDTO toCreate) throws SQLException {
		PatientDTO result = patientDao.create(toCreate);
		
		if(toCreate.getAcf() != null) {
			toCreate.getAcf().setLastReadDate(new Date());
			acfManager.updateLastModifiedDate(toCreate.getAcf().getId());
		}
		return result;
	}
	
	@Override
	@Transactional
	public PatientDTO update(PatientDTO toUpdate) throws SQLException {
		
		toUpdate.setLastReadDate(new Date());
		return patientDao.update(toUpdate);
	}
	
	@Override
	@Transactional
	public void delete(Long patientId) throws SQLException {
		patientDao.delete(patientId);
	}
	
	@Override
	@Transactional
	public void cleanupCache(Date oldestAllowedPatient) throws CacheCleanupException {
		try {
			patientDao.deleteItemsOlderThan(oldestAllowedPatient);
		} catch(SQLException sql) {
			throw new CacheCleanupException("Error cleaning up old patients in the database. Message is: " + sql.getMessage());
		} catch(Exception ex) {
			throw new CacheCleanupException(ex.getMessage());
		}
	}

	@Override
	@Transactional
	public PatientLocationMapDTO updatePatientLocationMap(PatientLocationMapDTO toUpdate)
			throws SQLException{
		return patientDao.updatePatientLocationMap(toUpdate);
	}
	
	@Override
	@Transactional
	public PatientLocationMapDTO createPatientLocationMap(PatientLocationMapDTO toCreate) 
			throws SQLException {
		return patientDao.createPatientLocationMap(toCreate);
	}

	@Override
	@Transactional
	public PatientLocationMapDTO createPatientLocationMapFromPatientRecord(PatientDTO patient, Long patientRecordId)
			throws SQLException {
		PatientLocationMapDTO result = null;
		PatientRecordDTO patientRecord = queryManager.getPatientRecordById(patientRecordId);
		
		if(patientRecord != null) {
			PatientLocationMapDTO patientLocationMapToCreate = new PatientLocationMapDTO();
			patientLocationMapToCreate.setPatientId(patient.getId());
			patientLocationMapToCreate.setExternalPatientRecordId(patientRecord.getLocationPatientRecordId());
			QueryLocationMapDTO queryLocationMapDto = queryDao.getQueryLocationById(patientRecord.getQueryLocationId());
			if(queryLocationMapDto != null) {
				patientLocationMapToCreate.setLocationId(queryLocationMapDto.getLocationId());	
			}
			result = patientDao.createPatientLocationMap(patientLocationMapToCreate);
			if(result.getLocationId() != null && result.getLocation() == null) {
				LocationDTO location = locationDao.findById(result.getLocationId());
				result.setLocation(location);
			}
		}
		return result;
	}
}
