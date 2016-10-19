package gov.ca.emsa.pulse.broker.dao.impl;

import gov.ca.emsa.pulse.broker.dao.PatientAddressLineDAO;
import gov.ca.emsa.pulse.broker.dto.AddressDTO;
import gov.ca.emsa.pulse.broker.dto.PatientAddressDTO;
import gov.ca.emsa.pulse.broker.dto.PatientAddressLineDTO;
import gov.ca.emsa.pulse.broker.entity.AddressEntity;
import gov.ca.emsa.pulse.broker.entity.PatientAddressEntity;
import gov.ca.emsa.pulse.broker.entity.PatientAddressLineEntity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.Query;

public class PatientAddressLineDAOImpl extends BaseDAOImpl implements PatientAddressLineDAO{
	
	@Override
	public PatientAddressLineDTO create(PatientAddressLineDTO dto) {
		PatientAddressLineEntity toInsert = new PatientAddressLineEntity();
		toInsert.setLine(dto.getLine());
		toInsert.setLineOrder(dto.getLineOrder());
		toInsert.setPatientAddressId(dto.getPatientAddressId());
		
		entityManager.persist(toInsert);
		entityManager.flush();
		return new PatientAddressLineDTO(toInsert);
	}

	@Override
	public PatientAddressLineDTO update(PatientAddressLineDTO addressDto) {
		PatientAddressLineEntity address = this.getEntityById(addressDto.getId());

		address.setLine(addressDto.getLine());
		address.setLineOrder(addressDto.getLineOrder());
		address.setPatientAddressId(addressDto.getPatientAddressId());
		
		address = entityManager.merge(address);
		return new PatientAddressLineDTO(address);
	}
	
	@Override
	public void delete(Long patientAddressId) {
		PatientAddressLineEntity toDelete = getEntityById(patientAddressId);
		entityManager.remove(toDelete);
	}

	@Override
	public void deleteAllLines(Long patientAddressId) {
		List<PatientAddressLineEntity> toDelete = getEntitiesByPatientAddressId(patientAddressId);
		for(PatientAddressLineEntity deleteThis : toDelete){
			entityManager.remove(deleteThis);
		}
	}
	
	@Override
	public PatientAddressLineDTO getById(Long id) {
		PatientAddressLineEntity ae = this.getEntityById(id);
		PatientAddressLineDTO dto = null;
		if (ae != null){
			dto = new PatientAddressLineDTO(ae); 
		}
		return dto;
	}
	
	private PatientAddressLineEntity getEntityById(Long id) {
		PatientAddressLineEntity entity = null;
		
		Query query = entityManager.createQuery( "from PatientAddressLineEntity a where (id = :entityid)", PatientAddressLineEntity.class );
		query.setParameter("entityid", id);
		List<PatientAddressLineEntity> result = query.getResultList();
		
		if (result.size() >= 1){
			entity = result.get(0);
		} 
		
		return entity;
	}

	@Override
	public List<PatientAddressLineDTO> getByPatientAddressId(Long id) {
		List<PatientAddressLineDTO> dtos = new ArrayList<PatientAddressLineDTO>();
		List<PatientAddressLineEntity> ae = this.getEntitiesByPatientAddressId(id);
		
		if (ae != null){
			for(PatientAddressLineEntity entity : ae){
				dtos.add(new PatientAddressLineDTO(entity)); 
			}
		}
		return dtos;
	}
	
	private List<PatientAddressLineEntity> getEntitiesByPatientAddressId(Long patientAddressId) {
		Query query = entityManager.createQuery( "from PatientAddressLineEntity a where (patient_address_id = :entityid) ORDER BY line_order DESC ", PatientAddressLineEntity.class );
		query.setParameter("entityid", patientAddressId);
		List<PatientAddressLineEntity> result = query.getResultList();
		
		return result;
	}
	
}
