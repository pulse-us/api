package gov.ca.emsa.pulse.broker.dao.impl;

import org.springframework.stereotype.Repository;

import gov.ca.emsa.pulse.broker.entity.AuditSourceEntity;
import gov.ca.emsa.pulse.broker.dao.AuditSourceDAO;
import gov.ca.emsa.pulse.broker.dto.AuditSourceDTO;

@Repository
public class AuditSourceDAOImpl extends BaseDAOImpl implements AuditSourceDAO{
	
	public AuditSourceDTO createAuditSource(AuditSourceDTO dto){
		AuditSourceEntity auditSourceEntity = new AuditSourceEntity();
		auditSourceEntity.setAuditEnterpriseSiteId(dto.getAuditEnterpriseSiteId());
		auditSourceEntity.setAuditSourceId(dto.getAuditSourceId());
		auditSourceEntity.setAuditSourceTypeCode(dto.getAuditSourceTypeCode());
		entityManager.persist(auditSourceEntity);
		entityManager.flush();
		return new AuditSourceDTO(auditSourceEntity);
	}
	
}
