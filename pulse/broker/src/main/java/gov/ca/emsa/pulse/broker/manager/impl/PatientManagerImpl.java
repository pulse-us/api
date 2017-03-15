package gov.ca.emsa.pulse.broker.manager.impl;

import gov.ca.emsa.pulse.broker.cache.CacheCleanupException;
import gov.ca.emsa.pulse.broker.dao.EndpointDAO;
import gov.ca.emsa.pulse.broker.dao.PatientDAO;
import gov.ca.emsa.pulse.broker.dao.QueryDAO;
import gov.ca.emsa.pulse.broker.domain.EndpointTypeEnum;
import gov.ca.emsa.pulse.broker.dto.EndpointDTO;
import gov.ca.emsa.pulse.broker.dto.LocationDTO;
import gov.ca.emsa.pulse.broker.dto.PatientDTO;
import gov.ca.emsa.pulse.broker.dto.PatientEndpointMapDTO;
import gov.ca.emsa.pulse.broker.dto.PatientRecordDTO;
import gov.ca.emsa.pulse.broker.dto.QueryEndpointMapDTO;
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
	@Autowired private EndpointDAO endpointDao;
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
	public PatientEndpointMapDTO updatePatientEndpointMap(PatientEndpointMapDTO toUpdate)
			throws SQLException{
		return patientDao.updatePatientLocationMap(toUpdate);
	}

	@Override
	@Transactional
	public PatientEndpointMapDTO createEndpointMapForDocumentDiscovery(PatientDTO patient, Long patientRecordId)
			throws SQLException {
		PatientEndpointMapDTO result = null;
		PatientRecordDTO patientRecord = queryManager.getPatientRecordById(patientRecordId);
		
		if(patientRecord != null) {
			EndpointDTO documentDiscoveryEndpoint = null;
			PatientEndpointMapDTO patientEndpointMapToCreate = new PatientEndpointMapDTO();
			patientEndpointMapToCreate.setPatientId(patient.getId());
			patientEndpointMapToCreate.setExternalPatientRecordId(patientRecord.getEndpointPatientRecordId());
			//get the endpoint that was queried for the patient discovery
			QueryEndpointMapDTO queryEndpointMapDto = queryDao.getQueryEndpointById(patientRecord.getQueryEndpointId());
			Long patientDiscoveryEndpointId = queryEndpointMapDto.getEndpointId();
			//figure out which location this endpoint came from
			//we assume that any locations which share this patient discovery endpoint
			//will also have the same document query and document retrieve endpoints
			EndpointDTO patientDiscoveryEndpoint = endpointDao.findById(patientDiscoveryEndpointId);
			if(patientDiscoveryEndpoint != null) {
				List<LocationDTO> relatedLocations = patientDiscoveryEndpoint.getLocations();
				if(relatedLocations != null && relatedLocations.size() > 0) {
					LocationDTO firstRelatedLocation = relatedLocations.get(0);
					documentDiscoveryEndpoint = endpointDao.findByLocationIdAndType(firstRelatedLocation.getId(), EndpointTypeEnum.DOCUMENT_DISCOVERY);
				}
			}
			
			if(documentDiscoveryEndpoint != null) {
				patientEndpointMapToCreate.setEndpointId(documentDiscoveryEndpoint.getId());	
			}
			result = patientDao.createPatientEndpointMap(patientEndpointMapToCreate);
			result.setEndpoint(documentDiscoveryEndpoint);
		}
		return result;
	}
}
