package gov.ca.emsa.pulse.broker.dao.impl;

import gov.ca.emsa.pulse.broker.dao.GivenNameDAO;
import gov.ca.emsa.pulse.broker.dao.NameAssemblyDAO;
import gov.ca.emsa.pulse.broker.dao.NameRepresentationDAO;
import gov.ca.emsa.pulse.broker.dao.NameTypeDAO;
import gov.ca.emsa.pulse.broker.dao.PatientNameDAO;
import gov.ca.emsa.pulse.broker.dto.GivenNameDTO;
import gov.ca.emsa.pulse.broker.dto.NameAssemblyDTO;
import gov.ca.emsa.pulse.broker.dto.NameRepresentationDTO;
import gov.ca.emsa.pulse.broker.dto.NameTypeDTO;
import gov.ca.emsa.pulse.broker.dto.PatientNameDTO;
import gov.ca.emsa.pulse.broker.entity.AddressEntity;
import gov.ca.emsa.pulse.broker.entity.GivenNameEntity;
import gov.ca.emsa.pulse.broker.entity.NameAssemblyEntity;
import gov.ca.emsa.pulse.broker.entity.NameRepresentationEntity;
import gov.ca.emsa.pulse.broker.entity.NameTypeEntity;
import gov.ca.emsa.pulse.broker.entity.PatientNameEntity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class PatientNameDAOImpl extends BaseDAOImpl implements PatientNameDAO {
	private static final Logger logger = LogManager.getLogger(PatientNameDAOImpl.class);
	@Autowired GivenNameDAO givenDAO;
	@Autowired NameTypeDAO nameTypeDAO;
	@Autowired NameRepresentationDAO nameRepDAO;
	@Autowired NameAssemblyDAO nameAssemblyDAO;

	@Override
	public PatientNameDTO create(PatientNameDTO dto) {
		PatientNameEntity patient = new PatientNameEntity();
		patient.setFamilyName(dto.getFamilyName());
		ArrayList<GivenNameDTO> givensDTO = new ArrayList<GivenNameDTO>();
		if(dto.getGivenName() != null){
			for(GivenNameDTO givenDto : dto.getGivenName()){
				if(givenDto.getId() == null){
					GivenNameDTO givenName = givenDAO.create(givenDto);
					givensDTO.add(givenName);
				}
			}
			dto.setGivenName(givensDTO);
			//given name entities should exist now
			ArrayList<GivenNameEntity> givensEntity = new ArrayList<GivenNameEntity>();
			for(GivenNameDTO given: dto.getGivenName()){
				GivenNameEntity name = entityManager.find(GivenNameEntity.class, given.getId());
				givensEntity.add(name);
			}
			patient.setGivenNames(givensEntity);
		}
		patient.setSuffix(dto.getSuffix());
		patient.setPrefix(dto.getPrefix());
		patient.setProfSuffix(dto.getProfSuffix());
		if(dto.getNameType() != null){
			if(dto.getNameType().getId() == null){
				NameTypeDTO nameTypeDTO = nameTypeDAO.create(dto.getNameType());
				dto.setNameType(nameTypeDTO);
			}
			NameTypeEntity nameType = entityManager.find(NameTypeEntity.class, dto.getNameType().getId());
			patient.setNameType(nameType);
		}
		if(dto.getNameRepresentation() != null){
			if(dto.getNameType().getId() == null){
				NameRepresentationDTO nameRep = nameRepDAO.create(dto.getNameRepresentation());
				dto.setNameRepresentation(nameRep);
			}
			NameRepresentationEntity nameRep = entityManager.find(NameRepresentationEntity.class, dto.getNameRepresentation().getId());
			patient.setNameRepresentation(nameRep);
		}
		if(dto.getNameAssembly() != null){
			if(dto.getNameType().getId() == null){
				NameAssemblyDTO nameAssembly = nameAssemblyDAO.create(dto.getNameAssembly());
				dto.setNameAssembly(nameAssembly);
			}
			NameAssemblyEntity assembly = entityManager.find(NameAssemblyEntity.class, dto.getNameAssembly().getId());
			patient.setNameAssembly(assembly);
		}
		patient.setEffectiveDate(dto.getEffectiveDate());
		patient.setExpirationDate(dto.getExpirationDate());

		entityManager.persist(patient);
		entityManager.flush();
		return new PatientNameDTO(patient);
	}

	@Override
	public PatientNameDTO update(PatientNameDTO dto) {
		PatientNameEntity patient = this.getEntityById(dto.getId());
		patient.setFamilyName(dto.getFamilyName());
		ArrayList<GivenNameEntity> givens = new ArrayList<GivenNameEntity>();
		for(GivenNameDTO givenDto : dto.getGivenName()){
			GivenNameEntity givenName = new GivenNameEntity();
			givenName.setGivenName(givenDto.getGivenName());
			givenName.setId(givenDto.getId());
			givens.add(givenName);
		}
		patient.setGivenNames(givens);
		if(dto.getSuffix() != null)
			patient.setSuffix(dto.getSuffix());
		if(dto.getPrefix() != null)
			patient.setPrefix(dto.getPrefix());
		if(dto.getNameType() != null){
			NameTypeEntity nameType = new NameTypeEntity();
			nameType.setCode(dto.getNameType().getCode());
			nameType.setDescription(dto.getNameType().getDescription());
			nameType.setId(dto.getNameType().getId());
			patient.setNameType(nameType);
		}
		if(dto.getNameRepresentation() != null){
			NameRepresentationEntity nameRep = new NameRepresentationEntity();
			nameRep.setCode(dto.getNameType().getCode());
			nameRep.setDescription(dto.getNameType().getDescription());
			nameRep.setId(dto.getNameType().getId());
			patient.setNameRepresentation(nameRep);
		}
		if(dto.getNameAssembly() != null){
			NameAssemblyEntity nameAssembly = new NameAssemblyEntity();
			nameAssembly.setCode(dto.getNameType().getCode());
			nameAssembly.setDescription(dto.getNameType().getDescription());
			nameAssembly.setId(dto.getNameType().getId());
			patient.setNameAssembly(nameAssembly);
		}
		if(dto.getEffectiveDate() != null)
			patient.setEffectiveDate(dto.getEffectiveDate());
		if(dto.getExpirationDate() != null)
			patient.setExpirationDate(dto.getExpirationDate());

		patient = entityManager.merge(patient);
		entityManager.flush();
		return new PatientNameDTO(patient);
	}

	@Override
	public void delete(Long id) {
		PatientNameEntity toDelete = getEntityById(id);
		if(toDelete != null) {
			try {
				entityManager.remove(toDelete);
				entityManager.flush();
			} catch(Exception ex) {
				logger.error("Could not delete patient name. Was it removed before the delete method was called?", ex);
			}
		}
	}


	@Override
	public PatientNameDTO getById(Long id) {
		PatientNameDTO dto = null;
		PatientNameEntity pe = this.getEntityById(id);

		if (pe != null){
			dto = new PatientNameDTO(pe); 
		}
		return dto;
	}

	private PatientNameEntity getEntityById(Long id) {
		PatientNameEntity entity = null;

		Query query = entityManager.createQuery( "SELECT distinct patName from PatientNameEntity patName "
				+ "where patName.id = :entityid) ", 
				PatientNameEntity.class );

		query.setParameter("entityid", id);
		List<PatientNameEntity> patients = query.getResultList();
		if(patients.size() != 0) {
			entity = patients.get(0);
		}
		return entity;
	}

}
