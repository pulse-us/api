package gov.ca.emsa.pulse.broker.dao.impl;

import gov.ca.emsa.pulse.broker.dao.AddressDAO;
import gov.ca.emsa.pulse.broker.dao.PatientDAO;
import gov.ca.emsa.pulse.broker.dao.PatientNameDAO;
import gov.ca.emsa.pulse.broker.dto.AddressDTO;
import gov.ca.emsa.pulse.broker.dto.GivenNameDTO;
import gov.ca.emsa.pulse.broker.dto.PatientDTO;
import gov.ca.emsa.pulse.broker.dto.PatientNameDTO;
import gov.ca.emsa.pulse.broker.dto.PatientOrganizationMapDTO;
import gov.ca.emsa.pulse.broker.entity.AddressEntity;
import gov.ca.emsa.pulse.broker.entity.AlternateCareFacilityEntity;
import gov.ca.emsa.pulse.broker.entity.GivenNameEntity;
import gov.ca.emsa.pulse.broker.entity.NameAssemblyEntity;
import gov.ca.emsa.pulse.broker.entity.NameRepresentationEntity;
import gov.ca.emsa.pulse.broker.entity.NameTypeEntity;
import gov.ca.emsa.pulse.broker.entity.PatientEntity;
import gov.ca.emsa.pulse.broker.entity.PatientNameEntity;
import gov.ca.emsa.pulse.broker.entity.PatientOrganizationMapEntity;
import gov.ca.emsa.pulse.common.domain.QueryStatus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class PatientDAOImpl extends BaseDAOImpl implements PatientDAO {
	private static final Logger logger = LogManager.getLogger(PatientDAOImpl.class);
	@Autowired AddressDAO addrDao;
	@Autowired PatientNameDAO nameDao;
	
	@Override
	public PatientDTO create(PatientDTO dto) {
		PatientEntity patient = new PatientEntity();
		if(dto.getPatientName() != null){
			if(dto.getPatientName().getId() == null){
				PatientNameDTO nameDto = nameDao.create(dto.getPatientName());
				dto.setPatientName(nameDto);
			}
			//patient name entity should exist now
			PatientNameEntity name = entityManager.find(PatientNameEntity.class, dto.getPatientName().getId());
			patient.setPatientName(name);
		}
		if(dto.getDateOfBirth() != null) {
			patient.setDateOfBirth(java.sql.Date.valueOf(dto.getDateOfBirth()));
		}
		patient.setSsn(dto.getSsn());
		patient.setGender(dto.getGender());
		patient.setPhoneNumber(dto.getPhoneNumber());
		if(dto.getAcf() != null) {
			patient.setAcfId(dto.getAcf().getId());
			AlternateCareFacilityEntity acf = entityManager.find(AlternateCareFacilityEntity.class, dto.getAcf().getId());
			patient.setAcf(acf);
		}
		patient.setLastReadDate(new Date());
		if(dto.getAddress() != null) {
			if(dto.getAddress().getId() == null) {
				AddressDTO addrDto = addrDao.create(dto.getAddress());
				dto.setAddress(addrDto);
			} 
			//address entity should exist now
			AddressEntity addr = entityManager.find(AddressEntity.class, dto.getAddress().getId());
			patient.setAddress(addr);
		}
		
		entityManager.persist(patient);
		entityManager.flush();
		return new PatientDTO(patient);
	}

	@Override
	public PatientOrganizationMapDTO createOrgMap(PatientOrganizationMapDTO toCreate) {
		PatientOrganizationMapEntity orgMap = new PatientOrganizationMapEntity();
		orgMap.setDocumentsQueryStatus(QueryStatus.ACTIVE.name());
		orgMap.setDocumentsQuerySuccess(null);
		orgMap.setDocumentsQueryStart(new Date());
		orgMap.setDocumentsQueryEnd(null);
		orgMap.setOrganizationId(toCreate.getOrganizationId());
		orgMap.setOrganizationPatientId(toCreate.getOrgPatientId());
		orgMap.setPatientId(toCreate.getPatientId());
		
		entityManager.persist(orgMap);
		entityManager.flush();
		return new PatientOrganizationMapDTO(orgMap);
	}
	
	@Override
	public PatientDTO update(PatientDTO dto) {
		PatientEntity patient = this.getEntityById(dto.getId());
		patient.getPatientName().setFamilyName(dto.getPatientName().getFamilyName());
		ArrayList<GivenNameEntity> givens = new ArrayList<GivenNameEntity>();
		for(GivenNameDTO givenDto : dto.getPatientName().getGivenName()){
			GivenNameEntity givenName = new GivenNameEntity();
			givenName.setGivenName(givenDto.getGivenName());
			givenName.setId(givenDto.getId());
			givenName.setPatientNameId(givenDto.getPatientNameId());
			givens.add(givenName);
		}
		patient.getPatientName().setGivenNames(givens);
		if(dto.getPatientName().getSuffix() != null)
			patient.getPatientName().setSuffix(dto.getPatientName().getSuffix());
		if(dto.getPatientName().getPrefix() != null)
			patient.getPatientName().setPrefix(dto.getPatientName().getPrefix());
		if(dto.getPatientName().getNameType() != null){
			NameTypeEntity nameType = new NameTypeEntity();
			nameType.setCode(dto.getPatientName().getNameType().getCode());
			nameType.setDescription(dto.getPatientName().getNameType().getDescription());
			nameType.setId(dto.getPatientName().getNameType().getId());
			patient.getPatientName().setNameType(nameType);
		}
		if(dto.getPatientName().getNameRepresentation() != null){
			NameRepresentationEntity nameRep = new NameRepresentationEntity();
			nameRep.setCode(dto.getPatientName().getNameType().getCode());
			nameRep.setDescription(dto.getPatientName().getNameType().getDescription());
			nameRep.setId(dto.getPatientName().getNameType().getId());
			patient.getPatientName().setNameRepresentation(nameRep);
		}
		if(dto.getPatientName().getNameAssembly() != null){
			NameAssemblyEntity nameAssembly = new NameAssemblyEntity();
			nameAssembly.setCode(dto.getPatientName().getNameType().getCode());
			nameAssembly.setDescription(dto.getPatientName().getNameType().getDescription());
			nameAssembly.setId(dto.getPatientName().getNameType().getId());
			patient.getPatientName().setNameAssembly(nameAssembly);
		}
		if(dto.getPatientName().getEffectiveDate() != null)
			patient.getPatientName().setEffectiveDate(dto.getPatientName().getEffectiveDate());
		if(dto.getPatientName().getExpirationDate() != null)
			patient.getPatientName().setExpirationDate(dto.getPatientName().getExpirationDate());
		if(dto.getDateOfBirth() != null) {
			patient.setDateOfBirth(java.sql.Date.valueOf(dto.getDateOfBirth()));
		} else {
			patient.setDateOfBirth(null);
		}
		patient.setSsn(dto.getSsn());
		patient.setGender(dto.getGender());
		patient.setPhoneNumber(dto.getPhoneNumber());
		if(dto.getAcf() != null) {
			patient.setAcfId(dto.getAcf().getId());
		} else {
			patient.setAcfId(null);
		}
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
	public PatientOrganizationMapDTO updateOrgMap(PatientOrganizationMapDTO toUpdate) {
		logger.debug("Looking up patient org map with id " + toUpdate.getId());
		PatientOrganizationMapEntity orgMap = getOrgMapById(toUpdate.getId());
		if(orgMap == null) {
			logger.error("Could not find patient org map with id " + toUpdate.getId());
		}
		
		orgMap.setDocumentsQueryStatus(toUpdate.getDocumentsQueryStatus());
		orgMap.setDocumentsQuerySuccess(toUpdate.getDocumentsQuerySuccess());
		orgMap.setDocumentsQueryStart(toUpdate.getDocumentsQueryStart());
		orgMap.setDocumentsQueryEnd(toUpdate.getDocumentsQueryEnd());
		orgMap.setOrganizationId(toUpdate.getOrganizationId());
		orgMap.setOrganizationPatientId(toUpdate.getOrgPatientId());
		orgMap.setPatientId(toUpdate.getPatientId());
		
		entityManager.merge(orgMap);
		entityManager.flush();
		return new PatientOrganizationMapDTO(orgMap);
	}
	
	@Override
	public void delete(Long id) {
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
		PatientOrganizationMapEntity entity = null;
		
		Query query = entityManager.createQuery( "SELECT pat from PatientOrganizationMapEntity pat "
				+ "LEFT OUTER JOIN FETCH pat.patient "
				+ "LEFT OUTER JOIN FETCH pat.organization "
				+ "where pat.id = :entityid) ", 
				PatientOrganizationMapEntity.class );
		
		query.setParameter("entityid", id);
		List<PatientOrganizationMapEntity> result = query.getResultList();
		if(result.size() == 1) {
			entity = result.get(0);
		}
		
		return new PatientOrganizationMapDTO(entity);
	}
		
	@Override
	public List<PatientDTO> getPatientsAtAcf(Long acfId) {
		Query query = entityManager.createQuery( "SELECT distinct pat from PatientEntity pat "
				+ "LEFT OUTER JOIN FETCH pat.acf "
				+ "LEFT OUTER JOIN FETCH pat.address "
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
	public void deleteItemsOlderThan(Date oldestDate) {			
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
				+ "LEFT OUTER JOIN FETCH pat.address "
				+ "LEFT OUTER JOIN FETCH pat.orgMaps "
				+ "where pat.id = :entityid) ", 
				PatientEntity.class );
		
		query.setParameter("entityid", id);
		List<PatientEntity> patients = query.getResultList();
		if(patients.size() != 0) {
			entity = patients.get(0);
		}
		return entity;
	}
	
	private PatientOrganizationMapEntity getOrgMapById(Long id) {		
		Query query = entityManager.createQuery( "SELECT distinct pat from PatientOrganizationMapEntity pat "
				+ "LEFT OUTER JOIN FETCH pat.organization "
				+ "LEFT OUTER JOIN FETCH pat.patient "
				+ "LEFT OUTER JOIN FETCH pat.documents "
				+ "where pat.id = :entityid) ", 
				PatientOrganizationMapEntity.class );
		
		query.setParameter("entityid", id);
		
		PatientOrganizationMapEntity entity = null;
		List<PatientOrganizationMapEntity> orgMaps = query.getResultList();
		if(orgMaps.size() != 0) {
			entity = orgMaps.get(0);
		}
		return entity;
	}
}