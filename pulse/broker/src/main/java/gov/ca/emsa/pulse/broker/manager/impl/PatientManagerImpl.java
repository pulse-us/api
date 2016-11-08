package gov.ca.emsa.pulse.broker.manager.impl;

import gov.ca.emsa.pulse.broker.cache.CacheCleanupException;
import gov.ca.emsa.pulse.broker.dao.AddressDAO;
import gov.ca.emsa.pulse.broker.dao.OrganizationDAO;
import gov.ca.emsa.pulse.broker.dao.PatientDAO;
import gov.ca.emsa.pulse.broker.dao.QueryDAO;
import gov.ca.emsa.pulse.broker.dto.AddressDTO;
import gov.ca.emsa.pulse.broker.dto.OrganizationDTO;
import gov.ca.emsa.pulse.broker.dto.PatientDTO;
import gov.ca.emsa.pulse.broker.dto.PatientOrganizationMapDTO;
import gov.ca.emsa.pulse.broker.dto.PatientRecordDTO;
import gov.ca.emsa.pulse.broker.dto.QueryOrganizationDTO;
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
	@Autowired private OrganizationDAO orgDao;
	@Autowired private AddressDAO addressDao;
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
			acfManager.update(toCreate.getAcf());
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
	public PatientOrganizationMapDTO updateOrganizationMap(PatientOrganizationMapDTO toUpdate)
			throws SQLException{
		return patientDao.updateOrgMap(toUpdate);
	}
	
	@Override
	@Transactional
	public PatientOrganizationMapDTO createOrganizationMap(PatientOrganizationMapDTO toCreate) 
			throws SQLException {
		return patientDao.createOrgMap(toCreate);
	}

	@Override
	@Transactional
	public PatientOrganizationMapDTO createOrganizationMapFromPatientRecord(PatientDTO patient, Long patientRecordId)
			throws SQLException {
		PatientOrganizationMapDTO result = null;
		PatientRecordDTO patientRecord = queryManager.getPatientRecordById(patientRecordId);
		
		if(patientRecord != null) {
			PatientOrganizationMapDTO orgMapToCreate = new PatientOrganizationMapDTO();
			orgMapToCreate.setPatientId(patient.getId());
			orgMapToCreate.setOrgPatientRecordId(patientRecord.getOrganizationPatientRecordId());
			QueryOrganizationDTO queryOrgDto = queryDao.getQueryOrganizationById(patientRecord.getQueryOrganizationId());
			if(queryOrgDto != null) {
				orgMapToCreate.setOrganizationId(queryOrgDto.getOrgId());	
			}
			result = patientDao.createOrgMap(orgMapToCreate);
			if(result.getOrganizationId() != null && result.getOrg() == null) {
				OrganizationDTO org = orgDao.findById(result.getOrganizationId());
				result.setOrg(org);
			}
		}
		return result;
	}
}
