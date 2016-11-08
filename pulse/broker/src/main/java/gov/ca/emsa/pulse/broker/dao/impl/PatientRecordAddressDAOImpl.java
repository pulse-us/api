package gov.ca.emsa.pulse.broker.dao.impl;

import gov.ca.emsa.pulse.broker.dao.PatientRecordAddressDAO;
import gov.ca.emsa.pulse.broker.dao.PatientRecordAddressLineDAO;
import gov.ca.emsa.pulse.broker.dto.AddressDTO;
import gov.ca.emsa.pulse.broker.dto.PatientRecordAddressDTO;
import gov.ca.emsa.pulse.broker.dto.PatientRecordAddressLineDTO;
import gov.ca.emsa.pulse.broker.entity.AddressEntity;
import gov.ca.emsa.pulse.broker.entity.PatientRecordAddressEntity;
import gov.ca.emsa.pulse.broker.entity.PatientRecordAddressLineEntity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class PatientRecordAddressDAOImpl extends BaseDAOImpl implements PatientRecordAddressDAO {
	@Autowired PatientRecordAddressLineDAO patientAddressLineDao;
	
	@Override
	public PatientRecordAddressDTO create(PatientRecordAddressDTO dto) {
		
		PatientRecordAddressEntity toInsert = new PatientRecordAddressEntity();
		
		toInsert.setCity(dto.getCity());
		toInsert.setState(dto.getState());
		toInsert.setZipcode(dto.getZipcode());
		toInsert.setPatientRecordId(dto.getPatientRecordId());
		
		entityManager.persist(toInsert);
		entityManager.flush();
		
		for(PatientRecordAddressLineDTO patientAddressLineDto : dto.getPatientRecordAddressLines()){
			patientAddressLineDto.setPatientRecordAddressId(toInsert.getId());
			patientAddressLineDao.create(patientAddressLineDto);
		}
		return new PatientRecordAddressDTO(toInsert);
	}

	@Override
	public PatientRecordAddressDTO update(PatientRecordAddressDTO addressDto) {
		PatientRecordAddressEntity address = this.getEntityById(addressDto.getId());

		for(PatientRecordAddressLineDTO patientAddressLineDto : addressDto.getPatientRecordAddressLines()){
			patientAddressLineDao.update(patientAddressLineDto);
		}
		address.setCity(addressDto.getCity());
		address.setState(addressDto.getState());
		address.setZipcode(addressDto.getZipcode());
		address.setPatientRecordId(addressDto.getPatientRecordId());
		
		address = entityManager.merge(address);
		return new PatientRecordAddressDTO(address);
	}

	@Override
	public void delete(Long id) {
		PatientRecordAddressEntity toDelete = getEntityById(id);
		entityManager.remove(toDelete);
	}

	@Override
	public List<PatientRecordAddressDTO> findAll() {
		List<PatientRecordAddressEntity> result = this.findAllEntities();
		List<PatientRecordAddressDTO> dtos = new ArrayList<PatientRecordAddressDTO>(result.size());
		for(PatientRecordAddressEntity entity : result) {
			dtos.add(new PatientRecordAddressDTO(entity));
		}
		return dtos;
	}
	
	@Override
	public PatientRecordAddressDTO getById(Long id) {
		
		PatientRecordAddressDTO dto = null;
		PatientRecordAddressEntity ae = this.getEntityById(id);
		
		if (ae != null){
			dto = new PatientRecordAddressDTO(ae); 
		}
		return dto;
	}

	@Override
	public PatientRecordAddressDTO getByValues(PatientRecordAddressDTO address) {
		PatientRecordAddressEntity ae = this.searchEntities(address);
		if(ae == null) {
			return null;
		}
		return new PatientRecordAddressDTO(ae);
	}
	
	private List<PatientRecordAddressEntity> findAllEntities() {
		Query query = entityManager.createQuery(  "SELECT a from PatientRecordAddressEntity"
												+ "LEFT OUTER JOIN FETCH a.patientRecordAddressLines ", PatientRecordAddressEntity.class );
		return query.getResultList();
	}
	
	private PatientRecordAddressEntity getEntityById(Long id) {
		PatientRecordAddressEntity entity = null;
		
		Query query = entityManager.createQuery(  "SELECT DISTINCT a "
												+ "from PatientRecordAddressEntity a "
												+ "LEFT OUTER JOIN FETCH a.patientRecordAddressLines "
												+ "where a.id = :entityid ", PatientRecordAddressEntity.class );
		query.setParameter("entityid", id);
		List<PatientRecordAddressEntity> result = query.getResultList();
		
		if(result.size() == 1) {
			entity = result.get(0);
		}
		
		return entity;
	}
	
	private PatientRecordAddressEntity getEntityByValues(PatientRecordAddressDTO address) {
		return this.searchEntities(address);
	}
	
	private PatientRecordAddressEntity searchEntities(PatientRecordAddressDTO toSearch) {
		PatientRecordAddressEntity entity = null;
		
		List<PatientRecordAddressLineDTO> addressLines = patientAddressLineDao.getByPatientAddressId(toSearch.getId());
		
		String addressQuery = "from PatientRecordAddressEntity a "
				+ " LEFT OUTER JOIN FETCH a.patientRecordAddressLines"
				+ " where (city = :city)"
				+ " AND (state = :state)"
				+ " AND (zipcode = :zipcode)";
		Query query = entityManager.createQuery(addressQuery, PatientRecordAddressEntity.class );
		query.setParameter("city", toSearch.getCity());
		query.setParameter("state", toSearch.getState());
		query.setParameter("zipcode", toSearch.getZipcode());
		
		List<PatientRecordAddressEntity> result = query.getResultList();
		
		if (result.size() >= 1){
			entity = result.get(0);
		}
		
		return entity;
	}
}
