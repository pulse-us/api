package gov.ca.emsa.pulse.broker.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.springframework.transaction.annotation.Transactional;

import gov.ca.emsa.pulse.broker.dao.PatientDAO;
import gov.ca.emsa.pulse.broker.dto.PatientDTO;
import gov.ca.emsa.pulse.broker.entity.AddressEntity;
import gov.ca.emsa.pulse.broker.entity.OrganizationEntity;
import gov.ca.emsa.pulse.broker.entity.PatientEntity;

public class PatientDAOImpl extends BaseDAOImpl implements PatientDAO {
	
	@Override
	public PatientDTO create(PatientDTO dto) {
		
		PatientEntity patient = new PatientEntity();
		patient.setFirstName(dto.getFirstName());
		patient.setLastName(dto.getLastName());
		patient.setDateOfBirth(dto.getDateOfBirth());
		patient.setSsn(dto.getSsn());
		patient.setGender(dto.getGender());
		patient.setPhoneNumber(dto.getPhoneNumber());
		patient.setPatientId(dto.getPatientId());
		if(dto.getOrganization() != null) {
			OrganizationEntity org = new OrganizationEntity();
			org.setId(dto.getOrganization().getId());
			patient.setOrganization(org);
		}
		if(dto.getAddress() != null) {
			AddressEntity add = new AddressEntity();
			add.setId(dto.getAddress().getId());
			patient.setAddress(add);
		}
		
		entityManager.persist(patient);
		entityManager.flush();
		return new PatientDTO(patient);
	}

	@Override
	@Transactional
	public PatientDTO update(PatientDTO dto) {
		PatientEntity patient = this.getEntityById(dto.getId());
		patient.setFirstName(dto.getFirstName());
		patient.setLastName(dto.getLastName());
		patient.setDateOfBirth(dto.getDateOfBirth());
		patient.setSsn(dto.getSsn());
		patient.setGender(dto.getGender());
		patient.setPhoneNumber(dto.getPhoneNumber());
		patient.setPatientId(dto.getPatientId());
		if(dto.getOrganization() != null) {
			OrganizationEntity org = new OrganizationEntity();
			org.setId(dto.getOrganization().getId());
			patient.setOrganization(org);
		}
		if(dto.getAddress() != null) {
			AddressEntity add = new AddressEntity();
			add.setId(dto.getAddress().getId());
			patient.setAddress(add);
		}
		
		patient = entityManager.merge(patient);
		return new PatientDTO(patient);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		PatientEntity toDelete = getEntityById(id);
		entityManager.remove(toDelete);
	}

	@Override
	public List<PatientDTO> findAll() {
		List<PatientEntity> result = this.findAllEntities();
		List<PatientDTO> dtos = new ArrayList<PatientDTO>(result.size());
		for(PatientEntity entity : result) {
			dtos.add(new PatientDTO(entity));
		}
		return dtos;
	}
	
	@Override
	public PatientDTO getById(Long id) {
		
		PatientDTO dto = null;
		PatientEntity pe = this.getEntityById(id);
		
		if (pe != null){
			dto = new PatientDTO(pe); 
		}
		return dto;
	}

	@Override
	public List<PatientDTO> getByValues(PatientDTO pat) {
		List<PatientEntity> patients = getEntityByPatientId(pat);
		List<PatientDTO> results = new ArrayList<PatientDTO>(patients.size());
		
		for(PatientEntity pEntity : patients) {
			PatientDTO patDto = new PatientDTO(pEntity);
			results.add(patDto);
		}
		return results;
	}
	
	private List<PatientEntity> findAllEntities() {
		Query query = entityManager.createQuery("from PatientEntity");
		return query.getResultList();
	}
	
	private PatientEntity getEntityById(Long id) {
		PatientEntity entity = null;
		
		Query query = entityManager.createQuery( "SELECT pat from PatientEntity pat "
				+ "LEFT OUTER JOIN FETCH pat.address "
				+ "LEFT OUTER JOIN FETCH pat.organization "
				+ "where pat.id = :entityid) ", 
				PatientEntity.class );
		
		query.setParameter("entityid", id);
		List<PatientEntity> result = query.getResultList();
		if(result.size() == 1) {
			entity = result.get(0);
		}
		
		return entity;
	}
	
	private List<PatientEntity> getEntityByPatientId(PatientDTO patient) {		
		Query query = entityManager.createQuery( "SELECT pat from PatientEntity pat "
				+ "LEFT OUTER JOIN FETCH pat.address "
				+ "LEFT OUTER JOIN FETCH pat.organization "
				+ "where pat.patientId LIKE :patientId) ", 
				PatientEntity.class );
		
		query.setParameter("patientId", patient.getPatientId());
		return query.getResultList();
	}
}
