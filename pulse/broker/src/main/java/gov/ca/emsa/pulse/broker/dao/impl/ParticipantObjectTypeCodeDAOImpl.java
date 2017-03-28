package gov.ca.emsa.pulse.broker.dao.impl;

import gov.ca.emsa.pulse.broker.dao.ParticipantObjectTypeCodeDAO;
import gov.ca.emsa.pulse.broker.dto.ParticipantObjectTypeCodeDTO;
import gov.ca.emsa.pulse.broker.entity.ParticipantObjectTypeCodeEntity;

import java.util.List;

import javax.persistence.Query;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

@Repository
public class ParticipantObjectTypeCodeDAOImpl extends BaseDAOImpl implements ParticipantObjectTypeCodeDAO {
	private static final Logger logger = LogManager.getLogger(ParticipantObjectTypeCodeDAOImpl.class);

	@Override
	public ParticipantObjectTypeCodeDTO create(ParticipantObjectTypeCodeDTO dto) {
		ParticipantObjectTypeCodeEntity patient = new ParticipantObjectTypeCodeEntity();
		patient.setCode(dto.getCode());
		patient.setDescription(dto.getDescription());
		patient.setId(dto.getId());
		
		entityManager.persist(patient);
		entityManager.flush();
		return new ParticipantObjectTypeCodeDTO(patient);
	}

	@Override
	public ParticipantObjectTypeCodeDTO update(ParticipantObjectTypeCodeDTO dto) {
		ParticipantObjectTypeCodeEntity patient = this.getEntityById(dto.getId());
		if(dto.getCode() != null){
			patient.setCode(dto.getCode());
		}
		if(dto.getDescription() != null){
			patient.setDescription(dto.getDescription());
		}

		patient = entityManager.merge(patient);
		entityManager.flush();
		return new ParticipantObjectTypeCodeDTO(patient);
	}

	@Override
	public void delete(Long id) {
		ParticipantObjectTypeCodeEntity toDelete = getEntityById(id);
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
	public ParticipantObjectTypeCodeDTO getById(Long id) {
		ParticipantObjectTypeCodeDTO dto = null;
		ParticipantObjectTypeCodeEntity pe = this.getEntityById(id);

		if (pe != null){
			dto = new ParticipantObjectTypeCodeDTO(pe); 
		}
		return dto;
	}
	
	@Override
	public ParticipantObjectTypeCodeDTO getByCode(String code) {
		ParticipantObjectTypeCodeDTO dto = null;
		ParticipantObjectTypeCodeEntity pe = this.getEntityByCode(code);

		if (pe != null){
			dto = new ParticipantObjectTypeCodeDTO(pe); 
		}
		return dto;
	}
	
	private ParticipantObjectTypeCodeEntity getEntityByCode(String code) {
		ParticipantObjectTypeCodeEntity entity = null;

		Query query = entityManager.createQuery( "SELECT distinct typeCode from ParticipantObjectTypeCodeEntity typeCode "
				+ "where typeCode.code = :entityid) ", 
				ParticipantObjectTypeCodeEntity.class );

		query.setParameter("entityid", code);
		List<ParticipantObjectTypeCodeEntity> patients = query.getResultList();
		if(patients.size() != 0) {
			entity = patients.get(0);
		}
		return entity;
	}

	private ParticipantObjectTypeCodeEntity getEntityById(Long id) {
		ParticipantObjectTypeCodeEntity entity = null;

		Query query = entityManager.createQuery( "SELECT distinct typeCode from ParticipantObjectTypeCodeEntity typeCode "
				+ "where typeCode.id = :entityid) ", 
				ParticipantObjectTypeCodeEntity.class );

		query.setParameter("entityid", id);
		List<ParticipantObjectTypeCodeEntity> patients = query.getResultList();
		if(patients.size() != 0) {
			entity = patients.get(0);
		}
		return entity;
	}
}
