package gov.ca.emsa.pulse.broker.dao.impl;

import gov.ca.emsa.pulse.broker.dao.PatientAddressDAO;
import gov.ca.emsa.pulse.broker.dao.PatientAddressLineDAO;
import gov.ca.emsa.pulse.broker.dto.AddressDTO;
import gov.ca.emsa.pulse.broker.dto.PatientAddressDTO;
import gov.ca.emsa.pulse.broker.dto.PatientAddressLineDTO;
import gov.ca.emsa.pulse.broker.entity.AddressEntity;
import gov.ca.emsa.pulse.broker.entity.PatientAddressEntity;
import gov.ca.emsa.pulse.broker.entity.PatientAddressLineEntity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;

public class PatientAddressDAOImpl extends BaseDAOImpl implements PatientAddressDAO {
	@Autowired PatientAddressLineDAO patientAddressLineDao;
	
	@Override
	public PatientAddressDTO create(PatientAddressDTO dto) {
		
		PatientAddressEntity toInsert = new PatientAddressEntity();
		for(PatientAddressLineDTO patientAddressLineDto : dto.getPatientAddressLines()){
			patientAddressLineDao.create(patientAddressLineDto);
		}
		toInsert.setCity(dto.getCity());
		toInsert.setState(dto.getState());
		toInsert.setZipcode(dto.getZipcode());
		
		entityManager.persist(toInsert);
		entityManager.flush();
		return new PatientAddressDTO(toInsert);
	}

	@Override
	public PatientAddressDTO update(PatientAddressDTO addressDto) {
		PatientAddressEntity address = this.getEntityById(addressDto.getId());

		for(PatientAddressLineDTO patientAddressLineDto : addressDto.getPatientAddressLines()){
			patientAddressLineDao.update(patientAddressLineDto);
		}
		address.setCity(addressDto.getCity());
		address.setState(addressDto.getState());
		address.setZipcode(addressDto.getZipcode());
		
		address = entityManager.merge(address);
		return new PatientAddressDTO(address);
	}

	@Override
	public void delete(Long id) {
		PatientAddressEntity toDelete = getEntityById(id);
		entityManager.remove(toDelete);
	}

	@Override
	public List<PatientAddressDTO> findAll() {
		List<PatientAddressEntity> result = this.findAllEntities();
		List<PatientAddressDTO> dtos = new ArrayList<PatientAddressDTO>(result.size());
		for(PatientAddressEntity entity : result) {
			dtos.add(new PatientAddressDTO(entity));
		}
		return dtos;
	}
	
	@Override
	public PatientAddressDTO getById(Long id) {
		
		PatientAddressDTO dto = null;
		PatientAddressEntity ae = this.getEntityById(id);
		
		if (ae != null){
			dto = new PatientAddressDTO(ae); 
		}
		return dto;
	}

	@Override
	public PatientAddressDTO getByValues(PatientAddressDTO address) {
		PatientAddressEntity ae = this.searchEntities(address);
		if(ae == null) {
			return null;
		}
		return new PatientAddressDTO(ae);
	}
	
	private List<PatientAddressEntity> findAllEntities() {
		Query query = entityManager.createQuery("SELECT a from PatientAddressEntity");
		return query.getResultList();
	}
	
	private PatientAddressEntity getEntityById(Long id) {
		PatientAddressEntity entity = null;
		
		Query query = entityManager.createQuery( "from PatientAddressEntity a where (id = :entityid) ", PatientAddressEntity.class );
		query.setParameter("entityid", id);
		List<PatientAddressEntity> result = query.getResultList();
		
		if(result.size() == 1) {
			entity = result.get(0);
		}
		
		return entity;
	}
	
	private PatientAddressEntity getEntityByValues(PatientAddressDTO address) {
		return this.searchEntities(address);
	}
	
	private PatientAddressEntity searchEntities(PatientAddressDTO toSearch) {
		PatientAddressEntity entity = null;
		
		List<PatientAddressLineDTO> addressLines = patientAddressLineDao.getByPatientAddressId(toSearch.getId());
		
		String addressQuery = "from PatientAddressEntity a "
				+ " LEFT OUTER JOIN FETCH a.patientAddressLines"
				+ " where (city = :city)"
				+ " AND (state = :state)"
				+ " AND (zipcode = :zipcode)";
		Query query = entityManager.createQuery(addressQuery, PatientAddressEntity.class );
		query.setParameter("city", toSearch.getCity());
		query.setParameter("state", toSearch.getState());
		query.setParameter("zipcode", toSearch.getZipcode());
		
		List<PatientAddressEntity> result = query.getResultList();
		
		if (result.size() >= 1){
			entity = result.get(0);
		}
		
		return entity;
	}
}
