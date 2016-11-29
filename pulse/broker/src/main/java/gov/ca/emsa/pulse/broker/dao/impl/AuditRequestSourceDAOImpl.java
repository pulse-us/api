package gov.ca.emsa.pulse.broker.dao.impl;

import org.springframework.stereotype.Repository;

import gov.ca.emsa.pulse.broker.dao.AuditRequestSourceDAO;
import gov.ca.emsa.pulse.broker.dto.AuditEventDTO;
import gov.ca.emsa.pulse.broker.dto.AuditHumanRequestorDTO;
import gov.ca.emsa.pulse.broker.dto.AuditRequestSourceDTO;
import gov.ca.emsa.pulse.broker.entity.AuditHumanRequestorEntity;
import gov.ca.emsa.pulse.broker.entity.AuditRequestSourceEntity;

@Repository
public class AuditRequestSourceDAOImpl extends BaseDAOImpl implements AuditRequestSourceDAO{

	public AuditRequestSourceDTO createAuditRequestSource(AuditRequestSourceDTO dto){
		AuditRequestSourceEntity toInsertAuditRequestSourceEntity = new AuditRequestSourceEntity();
		toInsertAuditRequestSourceEntity.setAlternativeUserId(dto.getAlternativeUserId());
		toInsertAuditRequestSourceEntity.setId(dto.getId());
		toInsertAuditRequestSourceEntity.setNetworkAccessPointId(dto.getNetworkAccessPointId());
		toInsertAuditRequestSourceEntity.setNetworkAccessPointTypeCode(dto.getNetworkAccessPointTypeCode());
		toInsertAuditRequestSourceEntity.setRoleIdCode(dto.getRoleIdCode());
		toInsertAuditRequestSourceEntity.setUserId(dto.getUserId());
		toInsertAuditRequestSourceEntity.setUserIsRequestor(dto.isUserIsRequestor());
		toInsertAuditRequestSourceEntity.setUserName(dto.getUserName());
		entityManager.persist(toInsertAuditRequestSourceEntity);
		entityManager.flush();
		return new AuditRequestSourceDTO(toInsertAuditRequestSourceEntity);
	}

}
