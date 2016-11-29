package gov.ca.emsa.pulse.broker.dao.impl;

import org.springframework.stereotype.Repository;

import gov.ca.emsa.pulse.broker.dao.AuditQueryParametersDAO;
import gov.ca.emsa.pulse.broker.dto.AuditEventDTO;
import gov.ca.emsa.pulse.broker.dto.AuditQueryParametersDTO;
import gov.ca.emsa.pulse.broker.entity.AuditQueryParametersEntity;

@Repository
public class AuditQueryParametersDAOImpl extends BaseDAOImpl implements AuditQueryParametersDAO{
	
	public AuditQueryParametersDTO createAuditQueryParameters(AuditQueryParametersDTO dto){
		AuditQueryParametersEntity auditEntity = new AuditQueryParametersEntity();
		auditEntity.setParticipantObjectDataLifecycle(dto.getParticipantObjectDataLifecycle());
		auditEntity.setParticipantObjectDetail(dto.getParticipantObjectDetail());
		auditEntity.setParticipantObjectId(dto.getParticipantObjectId());
		auditEntity.setParticipantObjectIdTypeCode(dto.getParticipantObjectIdTypeCode());
		auditEntity.setParticipantObjectName(dto.getParticipantObjectName());
		auditEntity.setParticipantObjectQuery(dto.getParticipantObjectQuery());
		auditEntity.setParticipantObjectSensitivity(dto.getParticipantObjectSensitivity());
		auditEntity.setParticipantObjectTypeCode(dto.getParticipantObjectTypeCode());
		auditEntity.setParticipantObjectTypeCodeRole(dto.getParticipantObjectTypeCodeRole());
		entityManager.persist(auditEntity);
		entityManager.flush();
		return new AuditQueryParametersDTO(auditEntity);
	}

}
