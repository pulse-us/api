package gov.ca.emsa.pulse.broker.dao.impl;

import java.util.List;

import javax.persistence.Query;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import gov.ca.emsa.pulse.broker.dao.NameRepresentationDAO;
import gov.ca.emsa.pulse.broker.dto.NameRepresentationDTO;
import gov.ca.emsa.pulse.broker.entity.NameRepresentationEntity;
import gov.ca.emsa.pulse.broker.entity.NameAssemblyEntity;

@Repository
public class NameRepresentationDAOImpl extends BaseDAOImpl implements NameRepresentationDAO {
	private static final Logger logger = LogManager.getLogger(NameRepresentationDAOImpl.class);

	@Override
	public NameRepresentationDTO create(NameRepresentationDTO dto) {
		NameRepresentationEntity patient = new NameRepresentationEntity();
		patient.setCode(dto.getCode());
		patient.setDescription(dto.getDescription());
		patient.setId(dto.getId());
		
		entityManager.persist(patient);
		entityManager.flush();
		return new NameRepresentationDTO(patient);
	}

	@Override
	public NameRepresentationDTO update(NameRepresentationDTO dto) {
		NameRepresentationEntity patient = this.getEntityById(dto.getId());
		if(dto.getCode() != null){
			patient.setCode(dto.getCode());
		}
		if(dto.getDescription() != null){
			patient.setDescription(dto.getDescription());
		}

		patient = entityManager.merge(patient);
		entityManager.flush();
		return new NameRepresentationDTO(patient);
	}

	@Override
	public void delete(Long id) {
		NameRepresentationEntity toDelete = getEntityById(id);
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
	public NameRepresentationDTO getById(Long id) {
		NameRepresentationDTO dto = null;
		NameRepresentationEntity pe = this.getEntityById(id);

		if (pe != null){
			dto = new NameRepresentationDTO(pe); 
		}
		return dto;
	}

	private NameRepresentationEntity getEntityById(Long id) {
		NameRepresentationEntity entity = null;

		Query query = entityManager.createQuery( "SELECT distinct nameRep from NameRepresentationEntity nameRep "
				+ "where nameRep.id = :entityid) ", 
				NameRepresentationEntity.class );

		query.setParameter("entityid", id);
		List<NameRepresentationEntity> patients = query.getResultList();
		if(patients.size() != 0) {
			entity = patients.get(0);
		}
		return entity;
	}
}
