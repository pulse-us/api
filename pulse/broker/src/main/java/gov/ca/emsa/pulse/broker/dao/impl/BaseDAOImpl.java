package gov.ca.emsa.pulse.broker.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


public class BaseDAOImpl {

	@PersistenceContext
	protected EntityManager entityManager;
	
	public EntityManager getEntityManager() {
		return entityManager;
	}
	
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
}
