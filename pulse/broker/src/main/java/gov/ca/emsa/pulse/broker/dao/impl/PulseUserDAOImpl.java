package gov.ca.emsa.pulse.broker.dao.impl;

import gov.ca.emsa.pulse.broker.dao.PulseUserDAO;
import gov.ca.emsa.pulse.broker.dto.PulseUserDTO;
import gov.ca.emsa.pulse.broker.entity.PulseUserEntity;

import java.util.List;

import javax.persistence.Query;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

@Repository
public class PulseUserDAOImpl extends BaseDAOImpl implements PulseUserDAO{
	
	private static final Logger logger = LogManager.getLogger(GivenNameDAOImpl.class);

	@Override
	public PulseUserDTO create(PulseUserDTO dto) {
		PulseUserEntity user = new PulseUserEntity();
		user.setAssertion(dto.getAssertion());
		
		entityManager.persist(user);
		entityManager.flush();
		return new PulseUserDTO(user);
	}

	@Override
	public PulseUserDTO update(PulseUserDTO dto) {
		PulseUserEntity user = this.getEntityById(dto.getId());
		if(dto.getAssertion() != null){
			user.setAssertion(dto.getAssertion());
		}

		user = entityManager.merge(user);
		entityManager.flush();
		return new PulseUserDTO(user);
	}

	@Override
	public void delete(Long id) {
		PulseUserEntity toDelete = getEntityById(id);
		if(toDelete != null) {
			try {
				entityManager.remove(toDelete);
				entityManager.flush();
			} catch(Exception ex) {
				logger.error("Could not delete user name. Was it removed before the delete method was called?", ex);
			}
		}
	}


	@Override
	public PulseUserDTO getById(Long id) {
		PulseUserDTO dto = null;
		PulseUserEntity pe = this.getEntityById(id);

		if (pe != null){
			dto = new PulseUserDTO(pe); 
		}
		return dto;
	}

	private PulseUserEntity getEntityById(Long id) {
		PulseUserEntity entity = null;

		Query query = entityManager.createQuery( "SELECT distinct user from PulseUserEntity user "
				+ "where user.id = :entityid) ", 
				PulseUserEntity.class );

		query.setParameter("entityid", id);
		List<PulseUserEntity> users = query.getResultList();
		if(users.size() != 0) {
			entity = users.get(0);
		}
		return entity;
	}

}
