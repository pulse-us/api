package gov.ca.emsa.pulse.broker.dao.impl;

import gov.ca.emsa.pulse.broker.dao.EventActionCodeDAO;
import gov.ca.emsa.pulse.broker.dao.PulseEventActionCodeDAO;
import gov.ca.emsa.pulse.broker.dto.PulseEventActionCodeDTO;
import gov.ca.emsa.pulse.broker.entity.PulseEventActionCodeEntity;

import java.util.List;

import javax.persistence.Query;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

@Repository
public class PulseEventActionCodeDAOImpl extends BaseDAOImpl implements PulseEventActionCodeDAO {
	private static final Logger logger = LogManager.getLogger(PulseEventActionCodeDAOImpl.class);

	@Override
	public PulseEventActionCodeDTO create(PulseEventActionCodeDTO dto) {
		PulseEventActionCodeEntity patient = new PulseEventActionCodeEntity();
		patient.setCode(dto.getCode());
		patient.setDescription(dto.getDescription());
		patient.setId(dto.getId());
		
		entityManager.persist(patient);
		entityManager.flush();
		return new PulseEventActionCodeDTO(patient);
	}

	@Override
	public PulseEventActionCodeDTO update(PulseEventActionCodeDTO dto) {
		PulseEventActionCodeEntity patient = this.getEntityById(dto.getId());
		if(dto.getCode() != null){
			patient.setCode(dto.getCode());
		}
		if(dto.getDescription() != null){
			patient.setDescription(dto.getDescription());
		}

		patient = entityManager.merge(patient);
		entityManager.flush();
		return new PulseEventActionCodeDTO(patient);
	}

	@Override
	public void delete(Long id) {
		PulseEventActionCodeEntity toDelete = getEntityById(id);
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
	public PulseEventActionCodeDTO getById(Long id) {
		PulseEventActionCodeDTO dto = null;
		PulseEventActionCodeEntity pe = this.getEntityById(id);

		if (pe != null){
			dto = new PulseEventActionCodeDTO(pe); 
		}
		return dto;
	}
	
	@Override
	public PulseEventActionCodeDTO getByCode(String code) {
		PulseEventActionCodeDTO dto = null;
		PulseEventActionCodeEntity pe = this.getEntityByCode(code);

		if (pe != null){
			dto = new PulseEventActionCodeDTO(pe); 
		}
		return dto;
	}
	
	private PulseEventActionCodeEntity getEntityByCode(String code) {
		PulseEventActionCodeEntity entity = null;

		Query query = entityManager.createQuery( "SELECT distinct eventAction from PulseEventActionCodeEntity eventAction "
				+ "where eventAction.code = :entityid) ", 
				PulseEventActionCodeEntity.class );

		query.setParameter("entityid", code);
		List<PulseEventActionCodeEntity> patients = query.getResultList();
		if(patients.size() != 0) {
			entity = patients.get(0);
		}
		return entity;
	}

	private PulseEventActionCodeEntity getEntityById(Long id) {
		PulseEventActionCodeEntity entity = null;

		Query query = entityManager.createQuery( "SELECT distinct eventAction from PulseEventActionCodeEntity eventAction "
				+ "where eventAction.id = :entityid) ", 
				PulseEventActionCodeEntity.class );

		query.setParameter("entityid", id);
		List<PulseEventActionCodeEntity> patients = query.getResultList();
		if(patients.size() != 0) {
			entity = patients.get(0);
		}
		return entity;
	}
}
