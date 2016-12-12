package gov.ca.emsa.pulse.broker.dao.impl;

import gov.ca.emsa.pulse.broker.dao.ParticipantObjectTypeCodeRoleDAO;
import gov.ca.emsa.pulse.broker.dto.ParticipantObjectTypeCodeRoleDTO;
import gov.ca.emsa.pulse.broker.entity.ParticipantObjectTypeCodeRoleEntity;

import java.util.List;

import javax.persistence.Query;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

@Repository
public class ParticipantObjectTypeCodeRoleDAOImpl extends BaseDAOImpl implements ParticipantObjectTypeCodeRoleDAO {
	private static final Logger logger = LogManager.getLogger(ParticipantObjectTypeCodeRoleDAOImpl.class);

	@Override
	public ParticipantObjectTypeCodeRoleDTO create(ParticipantObjectTypeCodeRoleDTO dto) {
		ParticipantObjectTypeCodeRoleEntity patient = new ParticipantObjectTypeCodeRoleEntity();
		patient.setCode(dto.getCode());
		patient.setDescription(dto.getDescription());
		patient.setId(dto.getId());
		
		entityManager.persist(patient);
		entityManager.flush();
		return new ParticipantObjectTypeCodeRoleDTO(patient);
	}

	@Override
	public ParticipantObjectTypeCodeRoleDTO update(ParticipantObjectTypeCodeRoleDTO dto) {
		ParticipantObjectTypeCodeRoleEntity patient = this.getEntityById(dto.getId());
		if(dto.getCode() != null){
			patient.setCode(dto.getCode());
		}
		if(dto.getDescription() != null){
			patient.setDescription(dto.getDescription());
		}

		patient = entityManager.merge(patient);
		entityManager.flush();
		return new ParticipantObjectTypeCodeRoleDTO(patient);
	}

	@Override
	public void delete(Long id) {
		ParticipantObjectTypeCodeRoleEntity toDelete = getEntityById(id);
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
	public ParticipantObjectTypeCodeRoleDTO getById(Long id) {
		ParticipantObjectTypeCodeRoleDTO dto = null;
		ParticipantObjectTypeCodeRoleEntity pe = this.getEntityById(id);

		if (pe != null){
			dto = new ParticipantObjectTypeCodeRoleDTO(pe); 
		}
		return dto;
	}
	
	@Override
	public ParticipantObjectTypeCodeRoleDTO getByCode(String code) {
		ParticipantObjectTypeCodeRoleDTO dto = null;
		ParticipantObjectTypeCodeRoleEntity pe = this.getEntityByCode(code);

		if (pe != null){
			dto = new ParticipantObjectTypeCodeRoleDTO(pe); 
		}
		return dto;
	}
	
	private ParticipantObjectTypeCodeRoleEntity getEntityByCode(String code) {
		ParticipantObjectTypeCodeRoleEntity entity = null;

		Query query = entityManager.createQuery( "SELECT distinct typeCodeRole from ParticipantObjectTypeCodeRoleEntity typeCodeRole "
				+ "where typeCodeRole.code = :entityid) ", 
				ParticipantObjectTypeCodeRoleEntity.class );

		query.setParameter("entityid", code);
		List<ParticipantObjectTypeCodeRoleEntity> patients = query.getResultList();
		if(patients.size() != 0) {
			entity = patients.get(0);
		}
		return entity;
	}

	private ParticipantObjectTypeCodeRoleEntity getEntityById(Long id) {
		ParticipantObjectTypeCodeRoleEntity entity = null;

		Query query = entityManager.createQuery( "SELECT distinct typeCodeRole from ParticipantObjectTypeCodeRoleEntity typeCodeRole "
				+ "where typeCodeRole.id = :entityid) ", 
				ParticipantObjectTypeCodeRoleEntity.class );

		query.setParameter("entityid", id);
		List<ParticipantObjectTypeCodeRoleEntity> patients = query.getResultList();
		if(patients.size() != 0) {
			entity = patients.get(0);
		}
		return entity;
	}
}
