package gov.ca.emsa.pulse.broker.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import gov.ca.emsa.pulse.broker.dao.AuditDAO;
import gov.ca.emsa.pulse.broker.dto.AuditDTO;
import gov.ca.emsa.pulse.broker.entity.AuditEntity;

@Repository
public class AuditDAOImpl extends BaseDAOImpl implements AuditDAO {
	private static final Logger logger = LogManager.getLogger(AuditDAOImpl.class);
	
	@Override
	public AuditDTO create(AuditDTO dto) {
		AuditEntity aud = new AuditEntity();
		aud.setId(dto.getId());
		aud.setQueryType(dto.getQueryType());
		aud.setQuery(dto.getQuery());
		aud.setQuerent(dto.getQuerent());
		aud.setCreationDate(dto.getCreationDate());
		aud.setLastModifiedDate(dto.getLastModifiedDate());
		
		entityManager.persist(aud);
		entityManager.flush();
		return new AuditDTO(aud);
	}

	@Override
	public List<AuditDTO> findAll() {
		List<AuditEntity> result = this.findAllEntities();
		List<AuditDTO> dtos = new ArrayList<AuditDTO>(result.size());
		for(AuditEntity entity : result) {
			dtos.add(new AuditDTO(entity));
		}
		return dtos;
	}
	
	@Override
	public AuditDTO getById(Long id) {
		
		AuditDTO dto = null;
		AuditEntity pe = this.getEntityById(id);
		
		if (pe != null){
			dto = new AuditDTO(pe); 
		}
		return dto;
	}
	
	@Override
	public AuditDTO getByQuerent(String querent) {
		
		AuditDTO dto = null;
		AuditEntity pe = this.getEntityByQuerent(querent);
		
		if (pe != null){
			dto = new AuditDTO(pe); 
		}
		return dto;
	}
	
	private List<AuditEntity> findAllEntities() {
		Query query = entityManager.createQuery("from AuditEntity");
		return query.getResultList();
	}
	
	private AuditEntity getEntityById(Long id) {
		AuditEntity entity = null;
		
		Query query = entityManager.createQuery( "from AuditEntity aud"
				+ "where aud.id = :entityid) ", 
				AuditEntity.class );
		
		query.setParameter("entityid", id);
		List<AuditEntity> result = query.getResultList();
		if(result.size() == 1) {
			entity = result.get(0);
		}
		
		return entity;
	}
	
	private AuditEntity getEntityByQuerent(String querent) {
		AuditEntity entity = null;
		
		Query query = entityManager.createQuery( "from AuditEntity aud"
				+ "where aud.querent = :querent) ", 
				AuditEntity.class );
		
		query.setParameter("querent", querent);
		List<AuditEntity> result = query.getResultList();
		if(result.size() == 1) {
			entity = result.get(0);
		}
		
		return entity;
	}
	
}
