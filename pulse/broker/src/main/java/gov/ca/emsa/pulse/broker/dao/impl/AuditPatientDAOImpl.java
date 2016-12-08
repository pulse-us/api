package gov.ca.emsa.pulse.broker.dao.impl;

import org.springframework.stereotype.Repository;

import gov.ca.emsa.pulse.broker.dao.AuditPatientDAO;
import gov.ca.emsa.pulse.broker.dto.AuditEventDTO;
import gov.ca.emsa.pulse.broker.dto.AuditPatientDTO;
import gov.ca.emsa.pulse.broker.entity.AuditPatientEntity;

@Repository
public class AuditPatientDAOImpl extends BaseDAOImpl implements AuditPatientDAO {
	
	public AuditPatientDTO createAuditPatient(AuditPatientDTO dto){
		AuditPatientEntity auditEntity = new AuditPatientEntity();
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
		return new AuditPatientDTO(auditEntity);
	}

}
