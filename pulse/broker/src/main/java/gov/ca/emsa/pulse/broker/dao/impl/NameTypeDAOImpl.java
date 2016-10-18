package gov.ca.emsa.pulse.broker.dao.impl;

import gov.ca.emsa.pulse.broker.dao.NameTypeDAO;
import gov.ca.emsa.pulse.broker.dto.NameTypeDTO;
import gov.ca.emsa.pulse.broker.entity.NameTypeEntity;

import java.util.List;

import javax.persistence.Query;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

@Repository
public class NameTypeDAOImpl extends BaseDAOImpl implements NameTypeDAO {
	private static final Logger logger = LogManager.getLogger(NameTypeDAOImpl.class);

	@Override
	public NameTypeDTO create(NameTypeDTO dto) {
		NameTypeEntity patient = new NameTypeEntity();
		patient.setCode(dto.getCode());
		patient.setDescription(dto.getDescription());
		patient.setId(dto.getId());
		
		entityManager.persist(patient);
		entityManager.flush();
		return new NameTypeDTO(patient);
	}

	@Override
	public NameTypeDTO update(NameTypeDTO dto) {
		NameTypeEntity patient = this.getEntityById(dto.getId());
		if(dto.getCode() != null){
			patient.setCode(dto.getCode());
		}
		if(dto.getDescription() != null){
			patient.setDescription(dto.getDescription());
		}

		patient = entityManager.merge(patient);
		entityManager.flush();
		return new NameTypeDTO(patient);
	}

	@Override
	public void delete(Long id) {
		NameTypeEntity toDelete = getEntityById(id);
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
	public NameTypeDTO getById(Long id) {
		NameTypeDTO dto = null;
		NameTypeEntity pe = this.getEntityById(id);

		if (pe != null){
			dto = new NameTypeDTO(pe); 
		}
		return dto;
	}

	private NameTypeEntity getEntityById(Long id) {
		NameTypeEntity entity = null;

		Query query = entityManager.createQuery( "SELECT distinct nameType from NameTypeEntity nameType "
				+ "where nameType.id = :entityid) ", 
				NameTypeEntity.class );

		query.setParameter("entityid", id);
		List<NameTypeEntity> patients = query.getResultList();
		if(patients.size() != 0) {
			entity = patients.get(0);
		}
		return entity;
	}
}
