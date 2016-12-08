package gov.ca.emsa.pulse.broker.dao.impl;

import org.springframework.stereotype.Repository;

import gov.ca.emsa.pulse.broker.dao.AuditRequestDestinationDAO;
import gov.ca.emsa.pulse.broker.dto.AuditEventDTO;
import gov.ca.emsa.pulse.broker.dto.AuditRequestDestinationDTO;
import gov.ca.emsa.pulse.broker.entity.AuditRequestDestinationEntity;

@Repository
public class AuditRequestDestinationDAOImpl extends BaseDAOImpl implements AuditRequestDestinationDAO{

	public AuditRequestDestinationDTO createAuditRequestDestination(AuditRequestDestinationDTO dto){
		AuditRequestDestinationEntity toInsertAuditRequestDestinationEntity = new AuditRequestDestinationEntity();
		toInsertAuditRequestDestinationEntity.setAlternativeUserId(dto.getAlternativeUserId());
		toInsertAuditRequestDestinationEntity.setNetworkAccessPointId(dto.getNetworkAccessPointId());
		toInsertAuditRequestDestinationEntity.setNetworkAccessPointTypeCode(dto.getNetworkAccessPointTypeCode());
		toInsertAuditRequestDestinationEntity.setRoleIdCode(dto.getRoleIdCode());
		toInsertAuditRequestDestinationEntity.setUserId(dto.getUserId());
		toInsertAuditRequestDestinationEntity.setUserIsRequestor(dto.isUserIsRequestor());
		toInsertAuditRequestDestinationEntity.setUserName(dto.getUserName());
		entityManager.persist(toInsertAuditRequestDestinationEntity);
		entityManager.flush();
		return new AuditRequestDestinationDTO(toInsertAuditRequestDestinationEntity);
	}

}
