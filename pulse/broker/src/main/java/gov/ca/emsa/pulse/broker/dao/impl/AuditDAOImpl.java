package gov.ca.emsa.pulse.broker.dao.impl;

import gov.ca.emsa.pulse.broker.dao.AuditDAO;
import gov.ca.emsa.pulse.broker.dto.AuditEventDTO;
import gov.ca.emsa.pulse.broker.entity.AuditEventEntity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

@Repository
public class AuditDAOImpl extends BaseDAOImpl implements AuditDAO {
	private static final Logger logger = LogManager.getLogger(AuditDAOImpl.class);
	
	@Override
	public AuditEventDTO createAuditEvent(AuditEventDTO dto) {
		AuditEventEntity audit = new AuditEventEntity();
		audit.setEventId(null);
		audit.setEventActionCode(null);
		audit.setEventDateTime(null);
		audit.setEventOutcomeIndicator(null);
		audit.setEventTypeCode(null);
		audit.setAuditRequestSourceId(null);
		audit.setAuditRequestDestinationId(null);
		audit.setAuditSourceId(null);
		audit.setAuditQueryParametersId(null);
		
		entityManager.persist(audit);
		entityManager.flush();
		return new AuditEventDTO(audit);
	}
	
	@Override
	public AuditEventDTO getAuditEventById(Long id) {
		
		AuditEventDTO dto = null;
		AuditEventEntity pe = this.getAuditEventEntityById(id);
		
		if (pe != null){
			dto = new AuditEventDTO(pe); 
		}
		return dto;
	}
	
	private AuditEventEntity getAuditEventEntityById(Long id) {
		AuditEventEntity entity = null;
		
		Query query = entityManager.createQuery( "from AuditEventEntity aud"
				+ "where aud.id = :entityid) ", 
				AuditEventEntity.class );
		
		query.setParameter("entityid", id);
		List<AuditEventEntity> result = query.getResultList();
		if(result.size() == 1) {
			entity = result.get(0);
		}
		
		return entity;
	}
	
	@Override
	public List<AuditEventDTO> findAllAuditEvents() {
		ArrayList<AuditEventDTO> dto = null;
		
		List<AuditEventEntity> pe = this.getAllAuditEventEntities();
		
		List<AuditEventDTO> dtos = new ArrayList<AuditEventDTO>();
		for(AuditEventEntity eventEnt : pe){
			dtos.add(new AuditEventDTO(eventEnt));
		}
		
		return dtos;
	}
	
	private List<AuditEventEntity> getAllAuditEventEntities() {
		
		Query query = entityManager.createQuery( "from AuditEventEntity", 
				AuditEventEntity.class );
		
		return query.getResultList();
	}
	
}
