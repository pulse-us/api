package gov.ca.emsa.pulse.broker.dao.impl;

import gov.ca.emsa.pulse.broker.dao.AddressDAO;
import gov.ca.emsa.pulse.broker.dto.AddressDTO;
import gov.ca.emsa.pulse.broker.entity.AddressEntity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

@Repository
public class AddressDAOImpl extends BaseDAOImpl implements AddressDAO {

	@Override
	public AddressDTO create(AddressDTO dto) {
		
		AddressEntity toInsert = new AddressEntity();
		toInsert.setStreetLineOne(dto.getStreetLineOne());
		toInsert.setStreetLineTwo(dto.getStreetLineTwo());
		toInsert.setCity(dto.getCity());
		toInsert.setState(dto.getState());
		toInsert.setZipcode(dto.getZipcode());
		toInsert.setCountry(dto.getCountry());
		
		entityManager.persist(toInsert);
		entityManager.flush();
		return new AddressDTO(toInsert);
	}

	@Override
	public AddressDTO update(AddressDTO addressDto) {
		AddressEntity address = this.getEntityById(addressDto.getId());

		address.setStreetLineOne(addressDto.getStreetLineOne());
		address.setStreetLineTwo(addressDto.getStreetLineTwo());
		address.setCity(addressDto.getCity());
		address.setState(addressDto.getState());
		address.setZipcode(addressDto.getZipcode());
		address.setCountry(addressDto.getCountry());
		
		address = entityManager.merge(address);
		return new AddressDTO(address);
	}

	@Override
	public void delete(Long id) {
		AddressEntity toDelete = getEntityById(id);
		entityManager.remove(toDelete);
	}

	@Override
	public List<AddressDTO> findAll() {
		List<AddressEntity> result = this.findAllEntities();
		List<AddressDTO> dtos = new ArrayList<AddressDTO>(result.size());
		for(AddressEntity entity : result) {
			dtos.add(new AddressDTO(entity));
		}
		return dtos;
	}
	
	@Override
	public AddressDTO getById(Long id) {
		
		AddressDTO dto = null;
		AddressEntity ae = this.getEntityById(id);
		
		if (ae != null){
			dto = new AddressDTO(ae); 
		}
		return dto;
	}

	@Override
	public AddressDTO getByValues(AddressDTO address) {
		AddressEntity ae = this.searchEntities(address);
		if(ae == null) {
			return null;
		}
		return new AddressDTO(ae);
	}
	
	private List<AddressEntity> findAllEntities() {
		Query query = entityManager.createQuery("SELECT a from AddressEntity");
		return query.getResultList();
	}
	
	private AddressEntity getEntityById(Long id) {
		AddressEntity entity = null;
		
		Query query = entityManager.createQuery( "from AddressEntity a where (id = :entityid) ", AddressEntity.class );
		query.setParameter("entityid", id);
		List<AddressEntity> result = query.getResultList();
		
		if(result.size() == 1) {
			entity = result.get(0);
		}
		
		return entity;
	}
	
	private AddressEntity getEntityByValues(AddressDTO address) {
		return this.searchEntities(address);
	}
	
	private AddressEntity searchEntities(AddressDTO toSearch) {
		AddressEntity entity = null;
		
		String addressQuery = "from AddressEntity a where "
				+ " (street_line_1 = :line1) "
				+ " AND (city = :city)"
				+ " AND (state = :state)"
				+ " AND (zipcode = :zipcode)"
				+ " AND (country = :country)";
		if(toSearch.getStreetLineTwo() != null) {
			addressQuery += " AND (street_line_2 = :line2)";
		} else {
			addressQuery += " AND (street_line_2 IS NULL)";
		}
		Query query = entityManager.createQuery(addressQuery, AddressEntity.class );
		query.setParameter("line1", toSearch.getStreetLineOne());
		query.setParameter("city", toSearch.getCity());
		query.setParameter("state", toSearch.getState());
		query.setParameter("zipcode", toSearch.getZipcode());
		query.setParameter("country", toSearch.getCountry());
		if(toSearch.getStreetLineTwo() != null) {
			query.setParameter("line2", toSearch.getStreetLineTwo());
		}
		
		List<AddressEntity> result = query.getResultList();
		
		if (result.size() >= 1){
			entity = result.get(0);
		} 
		
		return entity;
	}
}
