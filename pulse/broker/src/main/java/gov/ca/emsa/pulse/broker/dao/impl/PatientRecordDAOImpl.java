package gov.ca.emsa.pulse.broker.dao.impl;

import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import gov.ca.emsa.pulse.broker.dao.OrganizationDAO;
import gov.ca.emsa.pulse.broker.dao.PatientRecordDAO;
import gov.ca.emsa.pulse.broker.dto.PatientRecordDTO;
import gov.ca.emsa.pulse.broker.entity.PatientRecordEntity;

@Repository
public class PatientRecordDAOImpl extends BaseDAOImpl implements PatientRecordDAO {
	private static final Logger logger = LogManager.getLogger(PatientRecordDAOImpl.class);
	@Autowired OrganizationDAO orgDao;
	
	@Override
	public PatientRecordDTO create(PatientRecordDTO dto) {
		
		PatientRecordEntity patient = new PatientRecordEntity();
		patient.setFirstName(dto.getFirstName());
		patient.setLastName(dto.getLastName());
		patient.setDateOfBirth(dto.getDateOfBirth());
		patient.setSsn(dto.getSsn());
		patient.setGender(dto.getGender());
		patient.setPhoneNumber(dto.getPhoneNumber());
		patient.setOrgPatientId(dto.getOrgPatientId());
		if(dto.getAddress() != null) {
			patient.setStreetLineOne(dto.getAddress().getStreetLineOne());
			patient.setStreetLineTwo(dto.getAddress().getStreetLineTwo());
			patient.setCity(dto.getAddress().getCity());
			patient.setState(dto.getAddress().getState());
			patient.setZipcode(dto.getAddress().getZipcode());
			patient.setCountry(dto.getAddress().getCountry());
		}
		patient.setQueryOrganizationId(dto.getQueryOrganizationId());
		
		entityManager.persist(patient);
		entityManager.flush();
		return new PatientRecordDTO(patient);
	}

	@Override
	public PatientRecordDTO update(PatientRecordDTO dto) {
		PatientRecordEntity patient = this.getEntityById(dto.getId());
		patient.setFirstName(dto.getFirstName());
		patient.setLastName(dto.getLastName());
		patient.setDateOfBirth(dto.getDateOfBirth());
		patient.setSsn(dto.getSsn());
		patient.setGender(dto.getGender());
		patient.setPhoneNumber(dto.getPhoneNumber());
		patient.setOrgPatientId(dto.getOrgPatientId());
		patient.setLastModifiedDate(new Date());
		if(dto.getAddress() != null) {
			patient.setStreetLineOne(dto.getAddress().getStreetLineOne());
			patient.setStreetLineTwo(dto.getAddress().getStreetLineTwo());
			patient.setCity(dto.getAddress().getCity());
			patient.setState(dto.getAddress().getState());
			patient.setZipcode(dto.getAddress().getZipcode());
			patient.setCountry(dto.getAddress().getCountry());
		}
		patient.setQueryOrganizationId(dto.getQueryOrganizationId());
		
		patient = entityManager.merge(patient);
		entityManager.flush();
		return new PatientRecordDTO(patient);
	}

	@Override
	public void delete(Long id) {
		PatientRecordEntity toDelete = getEntityById(id);
		entityManager.remove(toDelete);
		entityManager.flush();
	}

	
	@Override
	public PatientRecordDTO getById(Long id) {
		
		PatientRecordDTO dto = null;
		PatientRecordEntity pe = this.getEntityById(id);
		
		if (pe != null){
			dto = new PatientRecordDTO(pe); 
		}
		return dto;
	}

	private PatientRecordEntity getEntityById(Long id) {
		PatientRecordEntity entity = null;
		
		Query query = entityManager.createQuery( "SELECT pat from PatientResultEntity pat "
				+ "LEFT OUTER JOIN FETCH pat.queryOrganization "
				+ "where pat.id = :entityid) ", 
				PatientRecordEntity.class );
		
		query.setParameter("entityid", id);
		List<PatientRecordEntity> result = query.getResultList();
		if(result.size() == 1) {
			entity = result.get(0);
		}
		
		return entity;
	}
}