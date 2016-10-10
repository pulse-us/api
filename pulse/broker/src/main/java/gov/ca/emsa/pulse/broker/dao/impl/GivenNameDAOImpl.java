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
import gov.ca.emsa.pulse.broker.dao.PatientDAO;
import gov.ca.emsa.pulse.broker.dao.GivenNameDAO;
import gov.ca.emsa.pulse.broker.dto.AddressDTO;
import gov.ca.emsa.pulse.broker.dto.GivenNameDTO;
import gov.ca.emsa.pulse.broker.dto.GivenNameDTO;
import gov.ca.emsa.pulse.broker.dto.PatientOrganizationMapDTO;
import gov.ca.emsa.pulse.common.domain.GivenName;
import gov.ca.emsa.pulse.common.domain.QueryStatus;
import gov.ca.emsa.pulse.broker.entity.AddressEntity;
import gov.ca.emsa.pulse.broker.entity.AlternateCareFacilityEntity;
import gov.ca.emsa.pulse.broker.entity.GivenNameEntity;
import gov.ca.emsa.pulse.broker.entity.PatientEntity;
import gov.ca.emsa.pulse.broker.entity.GivenNameEntity;
import gov.ca.emsa.pulse.broker.entity.PatientOrganizationMapEntity;

@Repository
public class GivenNameDAOImpl extends BaseDAOImpl implements GivenNameDAO {
	private static final Logger logger = LogManager.getLogger(GivenNameDAOImpl.class);

	@Override
	public GivenNameDTO create(GivenNameDTO dto) {
		GivenNameEntity patient = new GivenNameEntity();
		patient.setGivenName(dto.getGivenName());
		patient.setPatientNameId(dto.getPatientNameId());
		
		entityManager.persist(patient);
		entityManager.flush();
		return new GivenNameDTO(patient);
	}

	@Override
	public GivenNameDTO update(GivenNameDTO dto) {
		GivenNameEntity patient = this.getEntityById(dto.getId());
		if(dto.getGivenName() != null){
			patient.setGivenName(dto.getGivenName());
		}
		if(dto.getPatientNameId() != null){
			patient.setPatientNameId(dto.getPatientNameId());
		}

		patient = entityManager.merge(patient);
		entityManager.flush();
		return new GivenNameDTO(patient);
	}

	@Override
	public void delete(Long id) {
		GivenNameEntity toDelete = getEntityById(id);
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
	public GivenNameDTO getById(Long id) {
		GivenNameDTO dto = null;
		GivenNameEntity pe = this.getEntityById(id);

		if (pe != null){
			dto = new GivenNameDTO(pe); 
		}
		return dto;
	}

	private GivenNameEntity getEntityById(Long id) {
		GivenNameEntity entity = null;

		Query query = entityManager.createQuery( "SELECT distinct giveName from GivenNameEntity giveName "
				+ "where giveName.id = :entityid) ", 
				GivenNameEntity.class );

		query.setParameter("entityid", id);
		List<GivenNameEntity> patients = query.getResultList();
		if(patients.size() != 0) {
			entity = patients.get(0);
		}
		return entity;
	}

}

