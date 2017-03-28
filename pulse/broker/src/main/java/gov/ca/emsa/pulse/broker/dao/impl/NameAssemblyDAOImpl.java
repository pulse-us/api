package gov.ca.emsa.pulse.broker.dao.impl;

import gov.ca.emsa.pulse.broker.dao.NameAssemblyDAO;
import gov.ca.emsa.pulse.broker.dto.NameAssemblyDTO;
import gov.ca.emsa.pulse.broker.entity.NameAssemblyEntity;

import java.util.List;

import javax.persistence.Query;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

@Repository
public class NameAssemblyDAOImpl extends BaseDAOImpl implements NameAssemblyDAO {
	private static final Logger logger = LogManager.getLogger(NameAssemblyDAOImpl.class);

	@Override
	public NameAssemblyDTO create(NameAssemblyDTO dto) {
		NameAssemblyEntity patient = new NameAssemblyEntity();
		patient.setCode(dto.getCode());
		patient.setDescription(dto.getDescription());
		patient.setId(dto.getId());
		
		entityManager.persist(patient);
		entityManager.flush();
		return new NameAssemblyDTO(patient);
	}

	@Override
	public NameAssemblyDTO update(NameAssemblyDTO dto) {
		NameAssemblyEntity patient = this.getEntityById(dto.getId());
		if(dto.getCode() != null){
			patient.setCode(dto.getCode());
		}
		if(dto.getDescription() != null){
			patient.setDescription(dto.getDescription());
		}
		patient = entityManager.merge(patient);
		entityManager.flush();
		return new NameAssemblyDTO(patient);
	}

	@Override
	public void delete(Long id) {
		NameAssemblyEntity toDelete = getEntityById(id);
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
	public NameAssemblyDTO getByCode(String code) {
		NameAssemblyDTO dto = null;
		NameAssemblyEntity pe = this.getEntityByCode(code);

		if (pe != null){
			dto = new NameAssemblyDTO(pe); 
		}
		return dto;
	}

	private NameAssemblyEntity getEntityByCode(String code) {
		NameAssemblyEntity entity = null;

		Query query = entityManager.createQuery( "SELECT distinct nameAssembly from NameAssemblyEntity nameAssembly "
				+ "where nameAssembly.code = :entityid) ", 
				NameAssemblyEntity.class );

		query.setParameter("entityid", code);
		List<NameAssemblyEntity> patients = query.getResultList();
		if(patients.size() != 0) {
			entity = patients.get(0);
		}
		return entity;
	}

	@Override
	public NameAssemblyDTO getById(Long id) {
		NameAssemblyDTO dto = null;
		NameAssemblyEntity pe = this.getEntityById(id);

		if (pe != null){
			dto = new NameAssemblyDTO(pe); 
		}
		return dto;
	}

	private NameAssemblyEntity getEntityById(Long id) {
		NameAssemblyEntity entity = null;

		Query query = entityManager.createQuery( "SELECT distinct nameAssembly from NameAssemblyEntity nameAssembly "
				+ "where nameAssembly.id = :entityid) ", 
				NameAssemblyEntity.class );

		query.setParameter("entityid", id);
		List<NameAssemblyEntity> patients = query.getResultList();
		if(patients.size() != 0) {
			entity = patients.get(0);
		}
		return entity;
	}
}
