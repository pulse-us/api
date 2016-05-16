package gov.ca.emsa.pulse.broker.dao.impl;

import java.util.Date;
import java.util.List;
import javax.persistence.EntityExistsException;
import javax.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import gov.ca.emsa.pulse.broker.dao.OrganizationDAO;
import gov.ca.emsa.pulse.broker.dto.OrganizationDTO;
import gov.ca.emsa.pulse.broker.entity.OrganizationEntity;

@Repository
public class OrganizationDAOImpl extends BaseDAOImpl implements OrganizationDAO {
	
	public OrganizationDTO create(OrganizationDTO dto) throws EntityExistsException{

		OrganizationEntity entity = null;
		OrganizationDTO result = null;
		if(this.getOrganizationById(dto.getId()) != null){
			throw new EntityExistsException();
		}else{

			entity = new OrganizationEntity();
			entity.setId(dto.getId());
			entity.setName(dto.getName());
			entity.setCreationDate(new Date());
			entity.setLastModifiedDate(new Date());

			create(entity);

			result = new OrganizationDTO(entity);
		}
		return result;
	}
	
	public OrganizationDTO update(OrganizationDTO dto){

		OrganizationEntity entity =  this.getOrganizationById(dto.getId());

		if(!entity.getName().equals(dto.getName())){
			entity.setName(dto.getName());
			entity.setLastModifiedDate(new Date());

			update(entity);

			OrganizationDTO result = null;
			if (entity != null){
				result = new OrganizationDTO(entity);
			}

			return result;
		}else{
			return dto;
		}


	}
	
	private void create(OrganizationEntity entity) {
		
		entityManager.persist(entity);
		entityManager.flush();
		
	}
	
	private void update(OrganizationEntity entity) {

		entityManager.merge(entity);
		entityManager.flush();

	}
	
	
	
	@Override
	public OrganizationEntity getOrganizationById(Long id) {
		OrganizationEntity org = null;
		TypedQuery<OrganizationEntity> query = null;
		
		query = entityManager.createQuery("from OrganizationEntity where (id = :id) ", OrganizationEntity.class);
		
		query.setParameter("id", id);
		
		List<OrganizationEntity> result = query.getResultList();
		
		if(result.size() == 0){
			return null;
		}else if(result.size() == 1){
			org = result.get(0);
		}else{
			return null;
		}
		return org;
	}
	
}
