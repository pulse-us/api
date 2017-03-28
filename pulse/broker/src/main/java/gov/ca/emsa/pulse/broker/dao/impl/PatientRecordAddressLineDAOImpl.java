package gov.ca.emsa.pulse.broker.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import gov.ca.emsa.pulse.broker.dao.PatientRecordAddressLineDAO;
import gov.ca.emsa.pulse.broker.dto.PatientRecordAddressLineDTO;
import gov.ca.emsa.pulse.broker.entity.PatientRecordAddressLineEntity;

@Repository
public class PatientRecordAddressLineDAOImpl extends BaseDAOImpl implements PatientRecordAddressLineDAO{
	
	@Override
	public PatientRecordAddressLineDTO create(PatientRecordAddressLineDTO dto) {
		PatientRecordAddressLineEntity toInsert = new PatientRecordAddressLineEntity();
		toInsert.setLine(dto.getLine());
		toInsert.setLineOrder(dto.getLineOrder());
		toInsert.setPatientRecordAddressId(dto.getPatientRecordAddressId());
		
		entityManager.persist(toInsert);
		entityManager.flush();
		return new PatientRecordAddressLineDTO(toInsert);
	}

	@Override
	public PatientRecordAddressLineDTO update(PatientRecordAddressLineDTO addressDto) {
		PatientRecordAddressLineEntity address = this.getEntityById(addressDto.getId());

		address.setLine(addressDto.getLine());
		address.setLineOrder(addressDto.getLineOrder());
		address.setPatientRecordAddressId(addressDto.getPatientRecordAddressId());
		
		address = entityManager.merge(address);
		return new PatientRecordAddressLineDTO(address);
	}
	
	@Override
	public void delete(Long patientAddressId) {
		PatientRecordAddressLineEntity toDelete = getEntityById(patientAddressId);
		entityManager.remove(toDelete);
	}

	@Override
	public void deleteAllLines(Long patientAddressId) {
		List<PatientRecordAddressLineEntity> toDelete = getEntitiesByPatientAddressId(patientAddressId);
		for(PatientRecordAddressLineEntity deleteThis : toDelete){
			entityManager.remove(deleteThis);
		}
	}
	
	@Override
	public PatientRecordAddressLineDTO getById(Long id) {
		PatientRecordAddressLineEntity ae = this.getEntityById(id);
		PatientRecordAddressLineDTO dto = null;
		if (ae != null){
			dto = new PatientRecordAddressLineDTO(ae); 
		}
		return dto;
	}
	
	private PatientRecordAddressLineEntity getEntityById(Long id) {
		PatientRecordAddressLineEntity entity = null;
		
		Query query = entityManager.createQuery( "from PatientRecordAddressLineEntity a where (id = :entityid)", PatientRecordAddressLineEntity.class );
		query.setParameter("entityid", id);
		List<PatientRecordAddressLineEntity> result = query.getResultList();
		
		if (result.size() >= 1){
			entity = result.get(0);
		} 
		
		return entity;
	}

	@Override
	public List<PatientRecordAddressLineDTO> getByPatientAddressId(Long id) {
		List<PatientRecordAddressLineDTO> dtos = new ArrayList<PatientRecordAddressLineDTO>();
		List<PatientRecordAddressLineEntity> ae = this.getEntitiesByPatientAddressId(id);
		
		if (ae != null){
			for(PatientRecordAddressLineEntity entity : ae){
				dtos.add(new PatientRecordAddressLineDTO(entity)); 
			}
		}
		return dtos;
	}
	
	private List<PatientRecordAddressLineEntity> getEntitiesByPatientAddressId(Long patientAddressId) {
		Query query = entityManager.createQuery( "from PatientRecordAddressLineEntity a where (patient__record_address_id = :entityid) ORDER BY line_order DESC ", PatientRecordAddressLineEntity.class );
		query.setParameter("entityid", patientAddressId);
		List<PatientRecordAddressLineEntity> result = query.getResultList();
		
		return result;
	}
	
}
