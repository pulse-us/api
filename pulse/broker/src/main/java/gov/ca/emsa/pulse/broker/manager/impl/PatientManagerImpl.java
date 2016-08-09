package gov.ca.emsa.pulse.broker.manager.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	public PatientDTO create(PatientDTO toCreate) {
		PatientDTO result = patientDao.create(toCreate);
		
		if(toCreate.getAcf() != null) {
			toCreate.getAcf().setLastReadDate(new Date());
			acfManager.update(toCreate.getAcf());
		}
		return result;
	}
	
	@Override
	@Transactional
	public PatientDTO update(PatientDTO toUpdate) {
		if(toUpdate.getAddress() != null) {
			if(toUpdate.getAddress().getId() != null) {
				addressDao.update(toUpdate.getAddress());
			} else {
				AddressDTO createdAddress = addressDao.create(toUpdate.getAddress());
				toUpdate.setAddress(createdAddress);
			}
		}
		toUpdate.setLastReadDate(new Date());
		return patientDao.update(toUpdate);
	}
	
	@Override
	@Transactional
	public void delete(Long patientId) {
		patientDao.delete(patientId);
	}
	
	@Override
	@Transactional
	public void cleanupCache(Date oldestAllowedPatient) {
		patientDao.deleteItemsOlderThan(oldestAllowedPatient);
	}

	@Override
	@Transactional
	public PatientOrganizationMapDTO updateOrganizationMap(PatientOrganizationMapDTO toUpdate) {
		return patientDao.updateOrgMap(toUpdate);
	}
	
	@Override
	@Transactional
	public PatientOrganizationMapDTO createOrganizationMap(PatientOrganizationMapDTO toCreate) {
		return patientDao.createOrgMap(toCreate);
	}

	@Override
	@Transactional
	public PatientOrganizationMapDTO createOrganizationMapFromPatientRecord(PatientDTO patient, Long patientRecordId) {
		PatientOrganizationMapDTO result = null;
		PatientRecordDTO patientRecord = queryManager.getPatientRecordById(patientRecordId);
		
		if(patientRecord != null) {
			PatientOrganizationMapDTO orgMapToCreate = new PatientOrganizationMapDTO();
			orgMapToCreate.setPatientId(patient.getId());
			orgMapToCreate.setOrgPatientId(patientRecord.getOrgPatientId());
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
