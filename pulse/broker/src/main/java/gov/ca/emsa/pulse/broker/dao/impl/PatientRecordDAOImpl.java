package gov.ca.emsa.pulse.broker.dao.impl;

import gov.ca.emsa.pulse.broker.dao.OrganizationDAO;
import gov.ca.emsa.pulse.broker.dao.PatientNameDAO;
import gov.ca.emsa.pulse.broker.dao.PatientRecordDAO;
import gov.ca.emsa.pulse.broker.dto.GivenNameDTO;
import gov.ca.emsa.pulse.broker.dto.NameAssemblyDTO;
import gov.ca.emsa.pulse.broker.dto.NameRepresentationDTO;
import gov.ca.emsa.pulse.broker.dto.NameTypeDTO;
import gov.ca.emsa.pulse.broker.dto.PatientNameDTO;
import gov.ca.emsa.pulse.broker.dto.PatientRecordDTO;
import gov.ca.emsa.pulse.broker.entity.GivenNameEntity;
import gov.ca.emsa.pulse.broker.entity.NameAssemblyEntity;
import gov.ca.emsa.pulse.broker.entity.NameRepresentationEntity;
import gov.ca.emsa.pulse.broker.entity.NameTypeEntity;
import gov.ca.emsa.pulse.broker.entity.PatientNameEntity;
import gov.ca.emsa.pulse.broker.entity.PatientRecordEntity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class PatientRecordDAOImpl extends BaseDAOImpl implements PatientRecordDAO {
	private static final Logger logger = LogManager.getLogger(PatientRecordDAOImpl.class);
	@Autowired OrganizationDAO orgDao;
	@Autowired PatientNameDAO nameDao;

	@Override
	public PatientRecordDTO create(PatientRecordDTO dto) {

		PatientRecordEntity patient = new PatientRecordEntity();
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
		patient.setOrgPatientId(dto.getOrgPatientId());
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
		if(dto.getPatientName() != null){
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
		}
		if(dto.getDateOfBirth() != null) {
			patient.setDateOfBirth(java.sql.Date.valueOf(dto.getDateOfBirth()));
		} else {
			patient.setDateOfBirth(null);
		}
		patient.setSsn(dto.getSsn());
		patient.setGender(dto.getGender());
		patient.setPhoneNumber(dto.getPhoneNumber());
		patient.setOrgPatientId(dto.getOrgPatientId());
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

	private PatientRecordEntity getEntityById(Long id) {
		PatientRecordEntity entity = null;

		Query query = entityManager.createQuery( "SELECT pat from PatientRecordEntity pat "
				+ "LEFT OUTER JOIN FETCH pat.queryOrganization "
				+ "where pat.id = :entityid) ", 
				PatientRecordEntity.class );

		query.setParameter("entityid", id);
		List<PatientRecordEntity> result = query.getResultList();
		if(result.size() == 1) {
			entity = result.get(0);
		}

		return entity;
	}
}