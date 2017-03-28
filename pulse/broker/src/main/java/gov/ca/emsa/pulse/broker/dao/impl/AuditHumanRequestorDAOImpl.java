package gov.ca.emsa.pulse.broker.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import gov.ca.emsa.pulse.broker.dao.AuditHumanRequestorDAO;
import gov.ca.emsa.pulse.broker.dao.NetworkAccessPointTypeCodeDAO;
import gov.ca.emsa.pulse.broker.dto.AuditEventDTO;
import gov.ca.emsa.pulse.broker.dto.AuditHumanRequestorDTO;
import gov.ca.emsa.pulse.broker.dto.NetworkAccessPointTypeCodeDTO;
import gov.ca.emsa.pulse.broker.dto.ParticipantObjectTypeCodeDTO;
import gov.ca.emsa.pulse.broker.entity.AuditHumanRequestorEntity;
import gov.ca.emsa.pulse.broker.entity.NetworkAccessPointTypeCodeEntity;
import gov.ca.emsa.pulse.broker.entity.ParticipantObjectTypeCodeEntity;

@Repository
public class AuditHumanRequestorDAOImpl extends BaseDAOImpl implements AuditHumanRequestorDAO{
	
	
	public AuditHumanRequestorDTO createAuditHumanRequestor(AuditHumanRequestorDTO dto){
		
		AuditHumanRequestorEntity toInsertAuditHumanRequestorEntity = new AuditHumanRequestorEntity();
		if(dto != null){
			toInsertAuditHumanRequestorEntity.setAlternativeUserId(dto.getAlternativeUserId());
			toInsertAuditHumanRequestorEntity.setId(dto.getId());
			toInsertAuditHumanRequestorEntity.setAuditEventId(dto.getAuditEventId());
			toInsertAuditHumanRequestorEntity.setNetworkAccessPointId(dto.getNetworkAccessPointId());
			toInsertAuditHumanRequestorEntity.setNetworkAccessPointTypeCodeId(dto.getNetworkAccessPointTypeCodeId());
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
