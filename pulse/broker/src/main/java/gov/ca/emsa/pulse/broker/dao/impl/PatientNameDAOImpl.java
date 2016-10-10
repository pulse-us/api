package gov.ca.emsa.pulse.broker.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import gov.ca.emsa.pulse.broker.dao.AddressDAO;
import gov.ca.emsa.pulse.broker.dao.AlternateCareFacilityDAO;
import gov.ca.emsa.pulse.broker.dao.GivenNameDAO;
import gov.ca.emsa.pulse.broker.dao.PatientDAO;
import gov.ca.emsa.pulse.broker.dao.PatientNameDAO;
import gov.ca.emsa.pulse.broker.dto.AddressDTO;
import gov.ca.emsa.pulse.broker.dto.GivenNameDTO;
import gov.ca.emsa.pulse.broker.dto.PatientNameDTO;
import gov.ca.emsa.pulse.broker.dto.PatientOrganizationMapDTO;
import gov.ca.emsa.pulse.common.domain.PatientName;
import gov.ca.emsa.pulse.common.domain.QueryStatus;
import gov.ca.emsa.pulse.broker.entity.AddressEntity;
import gov.ca.emsa.pulse.broker.entity.AlternateCareFacilityEntity;
import gov.ca.emsa.pulse.broker.entity.GivenNameEntity;
import gov.ca.emsa.pulse.broker.entity.PatientEntity;
import gov.ca.emsa.pulse.broker.entity.PatientNameEntity;
import gov.ca.emsa.pulse.broker.entity.PatientOrganizationMapEntity;

@Repository
public class PatientNameDAOImpl extends BaseDAOImpl implements PatientNameDAO {
	private static final Logger logger = LogManager.getLogger(PatientNameDAOImpl.class);
	@Autowired GivenNameDAO givenDAO;

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
		patient.setNameTypeCode(dto.getNameTypeCode());
		patient.setNameTypeCodeDescription(dto.getNameTypeCodeDescription());
		patient.setNameRepresentationCode(dto.getNameRepresentationCode());
		patient.setNameRepresentationCodeDescription(dto.getNameRepresentationCodeDescription());
		patient.setNameAssemblyOrderCode(dto.getNameAssemblyOrderCode());
		patient.setNameAssemblyOrderCodeDescription(dto.getNameAssemblyOrderCodeDescription());
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
			givenName.setPatientNameId(givenDto.getPatientNameId());
			givens.add(givenName);
		}
		patient.setGivenNames(givens);
		if(dto.getSuffix() != null)
			patient.setSuffix(dto.getSuffix());
		if(dto.getPrefix() != null)
			patient.setPrefix(dto.getPrefix());
		if(dto.getNameTypeCode() != null)
			patient.setNameTypeCode(dto.getNameTypeCode());
		if(dto.getNameTypeCodeDescription() != null)
			patient.setNameTypeCodeDescription(dto.getNameTypeCodeDescription());
		if(dto.getNameRepresentationCode() != null)
			patient.setNameRepresentationCode(dto.getNameRepresentationCode());
		if(dto.getNameRepresentationCodeDescription() != null)
			patient.setNameRepresentationCodeDescription(dto.getNameRepresentationCodeDescription());
		if(dto.getNameAssemblyOrderCode() != null)
			patient.setNameAssemblyOrderCode(dto.getNameAssemblyOrderCode());
		if(dto.getNameAssemblyOrderCodeDescription() != null)
			patient.setNameAssemblyOrderCodeDescription(dto.getNameAssemblyOrderCodeDescription());
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
