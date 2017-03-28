package gov.ca.emsa.pulse.broker.dao.impl;

import gov.ca.emsa.pulse.broker.dao.PulseEventActionDAO;
import gov.ca.emsa.pulse.broker.dto.PulseEventActionDTO;
import gov.ca.emsa.pulse.broker.entity.PulseEventActionEntity;

import java.util.List;

import javax.persistence.Query;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

@Repository
public class PulseEventActionDAOImpl extends BaseDAOImpl implements PulseEventActionDAO {
	private static final Logger logger = LogManager.getLogger(PulseEventActionDAOImpl.class);

	@Override
	public PulseEventActionDTO create(PulseEventActionDTO dto) {
		PulseEventActionEntity patient = new PulseEventActionEntity();
		patient.setId(dto.getId());
		patient.setActionJson(dto.getActionJson());
		patient.setActionTStamp(dto.getActionTStamp());
		patient.setPulseEventActionCodeId(dto.getPulseEventActionCodeId());
		patient.setUsername(dto.getUsername());
		patient.setCreationDate(dto.getCreationDate());
		patient.setLastModifiedDate(dto.getLastModifiedDate());
		
		entityManager.persist(patient);
		entityManager.flush();
		return new PulseEventActionDTO(patient);
	}

	@Override
	public void delete(Long id) {
		PulseEventActionEntity toDelete = getEntityById(id);
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
	public PulseEventActionDTO getById(Long id) {
		PulseEventActionDTO dto = null;
		PulseEventActionEntity pe = this.getEntityById(id);

		if (pe != null){
			dto = new PulseEventActionDTO(pe); 
		}
		return dto;
	}

	private PulseEventActionEntity getEntityById(Long id) {
		PulseEventActionEntity entity = null;

		Query query = entityManager.createQuery( "SELECT distinct eventAction "
				+ "FROM PulseEventActionEntity eventAction "
				+ "JOIN FETCH PulseEventActionCodeEntity actionCode "
				+ "WHERE eventAction.id = :entityid) ", 
				PulseEventActionEntity.class );

		query.setParameter("entityid", id);
		List<PulseEventActionEntity> patients = query.getResultList();
		if(patients.size() != 0) {
			entity = patients.get(0);
		}
		return entity;
	}
}
