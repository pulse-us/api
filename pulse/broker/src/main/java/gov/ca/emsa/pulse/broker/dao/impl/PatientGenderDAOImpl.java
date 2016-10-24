package gov.ca.emsa.pulse.broker.dao.impl;

import gov.ca.emsa.pulse.broker.dao.PatientGenderDAO;
import gov.ca.emsa.pulse.broker.dto.PatientGenderDTO;
import gov.ca.emsa.pulse.broker.entity.PatientGenderEntity;

import java.util.List;

import javax.persistence.Query;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

@Repository
public class PatientGenderDAOImpl extends BaseDAOImpl implements PatientGenderDAO {
	private static final Logger logger = LogManager.getLogger(PatientGenderDAOImpl.class);

	@Override
	public PatientGenderDTO create(PatientGenderDTO dto) {
		PatientGenderEntity patient = new PatientGenderEntity();
		patient.setCode(dto.getCode());
		patient.setDescription(dto.getDescription());
		patient.setId(dto.getId());
		
		entityManager.persist(patient);
		entityManager.flush();
		return new PatientGenderDTO(patient);
	}

	@Override
	public PatientGenderDTO update(PatientGenderDTO dto) {
		PatientGenderEntity patient = this.getEntityById(dto.getId());
		if(dto.getCode() != null){
			patient.setCode(dto.getCode());
		}
		if(dto.getDescription() != null){
			patient.setDescription(dto.getDescription());
		}

		patient = entityManager.merge(patient);
		entityManager.flush();
		return new PatientGenderDTO(patient);
	}

	@Override
	public void delete(Long id) {
		PatientGenderEntity toDelete = getEntityById(id);
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
	public PatientGenderDTO getById(Long id) {
		PatientGenderDTO dto = null;
		PatientGenderEntity pe = this.getEntityById(id);

		if (pe != null){
			dto = new PatientGenderDTO(pe); 
		}
		return dto;
	}

	private PatientGenderEntity getEntityById(Long id) {
		PatientGenderEntity entity = null;

		Query query = entityManager.createQuery( "SELECT distinct nameType from PatientGenderEntity nameType "
				+ "where nameType.id = :entityid) ", 
				PatientGenderEntity.class );

		query.setParameter("entityid", id);
		List<PatientGenderEntity> patients = query.getResultList();
		if(patients.size() != 0) {
			entity = patients.get(0);
		}
		return entity;
	}
}
