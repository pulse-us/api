package gov.ca.emsa.pulse.broker.dao.impl;

import gov.ca.emsa.pulse.broker.dao.EventActionCodeDAO;
import gov.ca.emsa.pulse.broker.dto.EventActionCodeDTO;
import gov.ca.emsa.pulse.broker.entity.EventActionCodeEntity;

import java.util.List;

import javax.persistence.Query;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

@Repository
public class EventActionCodeDAOImpl extends BaseDAOImpl implements EventActionCodeDAO {
	private static final Logger logger = LogManager.getLogger(EventActionCodeDAOImpl.class);

	@Override
	public EventActionCodeDTO create(EventActionCodeDTO dto) {
		EventActionCodeEntity patient = new EventActionCodeEntity();
		patient.setCode(dto.getCode());
		patient.setDescription(dto.getDescription());
		patient.setId(dto.getId());
		
		entityManager.persist(patient);
		entityManager.flush();
		return new EventActionCodeDTO(patient);
	}

	@Override
	public EventActionCodeDTO update(EventActionCodeDTO dto) {
		EventActionCodeEntity patient = this.getEntityById(dto.getId());
		if(dto.getCode() != null){
			patient.setCode(dto.getCode());
		}
		if(dto.getDescription() != null){
			patient.setDescription(dto.getDescription());
		}

		patient = entityManager.merge(patient);
		entityManager.flush();
		return new EventActionCodeDTO(patient);
	}

	@Override
	public void delete(Long id) {
		EventActionCodeEntity toDelete = getEntityById(id);
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
	public EventActionCodeDTO getById(Long id) {
		EventActionCodeDTO dto = null;
		EventActionCodeEntity pe = this.getEntityById(id);

		if (pe != null){
			dto = new EventActionCodeDTO(pe); 
		}
		return dto;
	}
	
	@Override
	public EventActionCodeDTO getByCode(String code) {
		EventActionCodeDTO dto = null;
		EventActionCodeEntity pe = this.getEntityByCode(code);

		if (pe != null){
			dto = new EventActionCodeDTO(pe); 
		}
		return dto;
	}
	
	private EventActionCodeEntity getEntityByCode(String code) {
		EventActionCodeEntity entity = null;

		Query query = entityManager.createQuery( "SELECT distinct eventAction from EventActionCodeEntity eventAction "
				+ "where eventAction.code = :entityid) ", 
				EventActionCodeEntity.class );

		query.setParameter("entityid", code);
		List<EventActionCodeEntity> patients = query.getResultList();
		if(patients.size() != 0) {
			entity = patients.get(0);
		}
		return entity;
	}

	private EventActionCodeEntity getEntityById(Long id) {
		EventActionCodeEntity entity = null;

		Query query = entityManager.createQuery( "SELECT distinct eventAction from EventActionCodeEntity eventAction "
				+ "where eventAction.id = :entityid) ", 
				EventActionCodeEntity.class );

		query.setParameter("entityid", id);
		List<EventActionCodeEntity> patients = query.getResultList();
		if(patients.size() != 0) {
			entity = patients.get(0);
		}
		return entity;
	}
}
