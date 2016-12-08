package gov.ca.emsa.pulse.broker.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import gov.ca.emsa.pulse.broker.dao.AuditHumanRequestorDAO;
import gov.ca.emsa.pulse.broker.dto.AuditEventDTO;
import gov.ca.emsa.pulse.broker.dto.AuditHumanRequestorDTO;
import gov.ca.emsa.pulse.broker.entity.AuditHumanRequestorEntity;

@Repository
public class AuditHumanRequestorDAOImpl extends BaseDAOImpl implements AuditHumanRequestorDAO{

	public AuditHumanRequestorDTO createAuditHumanRequestor(AuditHumanRequestorDTO dto){
		
		AuditHumanRequestorEntity toInsertAuditHumanRequestorEntity = new AuditHumanRequestorEntity();
		if(dto != null){
			toInsertAuditHumanRequestorEntity.setAlternativeUserId(dto.getAlternativeUserId());
			toInsertAuditHumanRequestorEntity.setId(dto.getId());
			toInsertAuditHumanRequestorEntity.setAuditEventId(dto.getAuditEventId());
			toInsertAuditHumanRequestorEntity.setNetworkAccessPointId(dto.getNetworkAccessPointId());
			toInsertAuditHumanRequestorEntity.setNetworkAccessPointTypeCode(dto.getNetworkAccessPointTypeCode());
			toInsertAuditHumanRequestorEntity.setRoleIdCode(dto.getRoleIdCode());
			toInsertAuditHumanRequestorEntity.setUserId(dto.getUserId());
			toInsertAuditHumanRequestorEntity.setUserIsRequestor(dto.isUserIsRequestor());
			toInsertAuditHumanRequestorEntity.setUserName(dto.getUserName());
		}
		entityManager.persist(toInsertAuditHumanRequestorEntity);
		entityManager.flush();
		return new AuditHumanRequestorDTO(toInsertAuditHumanRequestorEntity);
	}

}
