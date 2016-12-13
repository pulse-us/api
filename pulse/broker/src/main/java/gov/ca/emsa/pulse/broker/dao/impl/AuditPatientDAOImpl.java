package gov.ca.emsa.pulse.broker.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import gov.ca.emsa.pulse.broker.dao.AuditPatientDAO;
import gov.ca.emsa.pulse.broker.dao.ParticipantObjectTypeCodeDAO;
import gov.ca.emsa.pulse.broker.dao.ParticipantObjectTypeCodeRoleDAO;
import gov.ca.emsa.pulse.broker.dto.AuditEventDTO;
import gov.ca.emsa.pulse.broker.dto.AuditPatientDTO;
import gov.ca.emsa.pulse.broker.dto.ParticipantObjectTypeCodeDTO;
import gov.ca.emsa.pulse.broker.dto.ParticipantObjectTypeCodeRoleDTO;
import gov.ca.emsa.pulse.broker.entity.AuditPatientEntity;
import gov.ca.emsa.pulse.broker.entity.ParticipantObjectTypeCodeEntity;
import gov.ca.emsa.pulse.broker.entity.ParticipantObjectTypeCodeRoleEntity;

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
		auditEntity.setParticipantObjectTypeCodeId(dto.getParticipantObjectTypeCodeId());
		auditEntity.setParticipantObjectTypeCodeRoleId(dto.getParticipantObjectTypeCodeRoleId());
		entityManager.persist(auditEntity);
		entityManager.flush();
		return new AuditPatientDTO(auditEntity);
	}

}
