package gov.ca.emsa.pulse.broker.dao.impl;

import gov.ca.emsa.pulse.broker.dao.OrganizationDAO;
import gov.ca.emsa.pulse.broker.dao.PatientRecordNameDAO;
import gov.ca.emsa.pulse.broker.dao.PatientRecordDAO;
import gov.ca.emsa.pulse.broker.dto.GivenNameDTO;
import gov.ca.emsa.pulse.broker.dto.NameAssemblyDTO;
import gov.ca.emsa.pulse.broker.dto.NameRepresentationDTO;
import gov.ca.emsa.pulse.broker.dto.NameTypeDTO;
import gov.ca.emsa.pulse.broker.dto.PatientOrganizationMapDTO;
import gov.ca.emsa.pulse.broker.dto.PatientRecordNameDTO;
import gov.ca.emsa.pulse.broker.dto.PatientRecordDTO;
import gov.ca.emsa.pulse.broker.entity.GivenNameEntity;
import gov.ca.emsa.pulse.broker.entity.NameAssemblyEntity;
import gov.ca.emsa.pulse.broker.entity.NameRepresentationEntity;
import gov.ca.emsa.pulse.broker.entity.NameTypeEntity;
import gov.ca.emsa.pulse.broker.entity.PatientOrganizationMapEntity;
import gov.ca.emsa.pulse.broker.entity.PatientRecordNameEntity;
import gov.ca.emsa.pulse.broker.entity.PatientRecordEntity;
import gov.ca.emsa.pulse.common.domain.QueryStatus;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Query;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class PatientRecordDAOImpl extends BaseDAOImpl implements PatientRecordDAO {
	private static final Logger logger = LogManager.getLogger(PatientRecordDAOImpl.class);
	@Autowired OrganizationDAO orgDao;
	@Autowired PatientRecordNameDAO nameDao;
	
	@Override
	public PatientOrganizationMapDTO createOrgMap(PatientOrganizationMapDTO toCreate) {
		PatientOrganizationMapEntity orgMap = new PatientOrganizationMapEntity();
		orgMap.setDocumentsQueryStatus(QueryStatus.ACTIVE.name());
		orgMap.setDocumentsQuerySuccess(null);
		orgMap.setDocumentsQueryStart(new Date());
		orgMap.setDocumentsQueryEnd(null);
		orgMap.setOrganizationId(toCreate.getOrganizationId());
		orgMap.setOrganizationPatientRecordId(toCreate.getOrgPatientRecordId());
		orgMap.setPatientRecordId(toCreate.getPatientId());
		
		entityManager.persist(orgMap);
		entityManager.flush();
		return new PatientOrganizationMapDTO(orgMap);
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
		orgMap.setOrganizationPatientRecordId(toUpdate.getOrgPatientRecordId());
		orgMap.setPatientRecordId(toUpdate.getPatientId());

		entityManager.merge(orgMap);
		entityManager.flush();
		return new PatientOrganizationMapDTO(orgMap);
	}

	@Override
	public PatientRecordDTO create(PatientRecordDTO dto) {
		PatientRecordEntity patient = new PatientRecordEntity();
		if(dto.getPatientRecordName() != null){
			for(PatientRecordNameDTO PatientRecordNameDTO : dto.getPatientRecordName()){
				if(PatientRecordNameDTO.getId() == null){
					PatientRecordNameDTO nameDto = nameDao.create(PatientRecordNameDTO);
					dto.getPatientRecordName().add(nameDto);
				}
				//patient name entity should exist now
				PatientRecordNameEntity name = entityManager.find(PatientRecordNameEntity.class, PatientRecordNameDTO.getId());
				patient.getPatientRecordName().add(name);
			}
		}
		if(dto.getDateOfBirth() != null) {
			patient.setDateOfBirth(java.sql.Date.valueOf(dto.getDateOfBirth()));
		}
		patient.setSsn(dto.getSsn());
		patient.setGender(dto.getGender());
		patient.setPhoneNumber(dto.getPhoneNumber());
		patient.setOrgPatientRecordId(dto.getOrgPatientRecordId());
		if(dto.getAddress() != null) {
			patient.setStreetLineOne(dto.getAddress().getStreetLineOne());
			patient.setStreetLineTwo(dto.getAddress().getStreetLineTwo());
			patient.setCity(dto.getAddress().getCity());
			patient.setState(dto.getAddress().getState());
			patient.setZipcode(dto.getAddress().getZipcode());
			patient.setCountry(dto.getAddress().getCountry());
		}
		patient.setQueryOrganizationId(dto.getQueryOrganizationId());

		entityManager.persist(patient);
		entityManager.flush();
		return new PatientRecordDTO(patient);
	}

	@Override
	public PatientRecordDTO update(PatientRecordDTO dto) {
		PatientRecordEntity patient = this.getEntityById(dto.getId());
		if(dto.getPatientRecordName() != null){
			for(PatientRecordNameDTO PatientRecordNameDTO : dto.getPatientRecordName()){
				PatientRecordNameEntity PatientRecordNameEntity = new PatientRecordNameEntity();
				PatientRecordNameEntity.setFamilyName(PatientRecordNameDTO.getFamilyName());
				Set<GivenNameEntity> givens = new HashSet<GivenNameEntity>();
				for(GivenNameDTO givenDto : PatientRecordNameDTO.getGivenName()){
					GivenNameEntity givenName = new GivenNameEntity();
					givenName.setGivenName(givenDto.getGivenName());
					givenName.setId(givenDto.getId());
					givens.add(givenName);
				}
				PatientRecordNameEntity.setGivenNames(givens);
				if(PatientRecordNameDTO.getSuffix() != null)
					PatientRecordNameEntity.setSuffix(PatientRecordNameDTO.getSuffix());
				if(PatientRecordNameDTO.getPrefix() != null)
					PatientRecordNameEntity.setPrefix(PatientRecordNameDTO.getPrefix());
				if(PatientRecordNameDTO.getNameType() != null){
					NameTypeEntity nameType = new NameTypeEntity();
					nameType.setCode(PatientRecordNameDTO.getNameType().getCode());
					nameType.setDescription(PatientRecordNameDTO.getNameType().getDescription());
					nameType.setId(PatientRecordNameDTO.getNameType().getId());
					PatientRecordNameEntity.setNameType(nameType);
				}
				if(PatientRecordNameDTO.getNameRepresentation() != null){
					NameRepresentationEntity nameRep = new NameRepresentationEntity();
					nameRep.setCode(PatientRecordNameDTO.getNameType().getCode());
					nameRep.setDescription(PatientRecordNameDTO.getNameType().getDescription());
					nameRep.setId(PatientRecordNameDTO.getNameType().getId());
					PatientRecordNameEntity.setNameRepresentation(nameRep);
				}
				if(PatientRecordNameDTO.getNameAssembly() != null){
					NameAssemblyEntity nameAssembly = new NameAssemblyEntity();
					nameAssembly.setCode(PatientRecordNameDTO.getNameType().getCode());
					nameAssembly.setDescription(PatientRecordNameDTO.getNameType().getDescription());
					nameAssembly.setId(PatientRecordNameDTO.getNameType().getId());
					PatientRecordNameEntity.setNameAssembly(nameAssembly);
				}
				if(PatientRecordNameDTO.getEffectiveDate() != null)
					PatientRecordNameEntity.setEffectiveDate(PatientRecordNameDTO.getEffectiveDate());
				if(PatientRecordNameDTO.getExpirationDate() != null)
					PatientRecordNameEntity.setExpirationDate(PatientRecordNameDTO.getExpirationDate());
				patient.getPatientRecordName().add(PatientRecordNameEntity);
			}
		}
		if(dto.getDateOfBirth() != null) {
			patient.setDateOfBirth(java.sql.Date.valueOf(dto.getDateOfBirth()));
		} else {
			patient.setDateOfBirth(null);
		}
		patient.setSsn(dto.getSsn());
		patient.setGender(dto.getGender());
		patient.setPhoneNumber(dto.getPhoneNumber());
		patient.setOrgPatientRecordId(dto.getOrgPatientRecordId());
		if(dto.getAddress() != null) {
			patient.setStreetLineOne(dto.getAddress().getStreetLineOne());
			patient.setStreetLineTwo(dto.getAddress().getStreetLineTwo());
			patient.setCity(dto.getAddress().getCity());
			patient.setState(dto.getAddress().getState());
			patient.setZipcode(dto.getAddress().getZipcode());
			patient.setCountry(dto.getAddress().getCountry());
		}
		patient.setQueryOrganizationId(dto.getQueryOrganizationId());

		patient = entityManager.merge(patient);
		entityManager.flush();
		return new PatientRecordDTO(patient);
	}

	@Override
	public void delete(Long id) {
		PatientRecordEntity toDelete = getEntityById(id);
		entityManager.remove(toDelete);
		entityManager.flush();
	}


	@Override
	public PatientRecordDTO getById(Long id) {

		PatientRecordDTO dto = null;
		PatientRecordEntity pe = this.getEntityById(id);

		if (pe != null){
			dto = new PatientRecordDTO(pe); 
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

	private PatientRecordEntity getEntityById(Long id) {
		PatientRecordEntity entity = null;
		
		Query query = entityManager.createQuery( "SELECT DISTINCT pat "
				+ "FROM PatientRecordEntity pat "
				+ "LEFT OUTER JOIN FETCH pat.queryOrganization "
				+ "LEFT OUTER JOIN FETCH pat.patientRecordName "
				+ "where pat.id = :entityid ", 
				PatientRecordEntity.class );

		query.setParameter("entityid", id);
		List<PatientRecordEntity> result = query.getResultList();
		if(result.size() == 1) {
			entity = result.get(0);
		}

		return entity;
	}
}