package gov.ca.emsa.pulse.broker.manager.impl;

import gov.ca.emsa.pulse.broker.audit.AuditEvent;
import gov.ca.emsa.pulse.broker.dao.AuditEventDAO;
import gov.ca.emsa.pulse.broker.domain.QueryType;
import gov.ca.emsa.pulse.broker.dto.AuditEventDTO;
import gov.ca.emsa.pulse.broker.manager.AuditEventManager;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuditEventManagerImpl implements AuditEventManager{
	
	@Autowired
	private AuditEventDAO auditEventDao;
	
	// Initiating gateway audit event
	@Override
	public AuditEventDTO addAuditEventEntryIG(AuditEventDTO ae) {
		AuditEventDTO auditEventDTO = new AuditEventDTO();
		auditEventDTO.setEventId(ae.getEventId());
		auditEventDTO.setEventActionCode(ae.getEventActionCode());
		auditEventDTO.setEventDateTime(ae.getEventDateTime());
		auditEventDTO.setEventOutcomeIndicator(ae.getEventOutcomeIndicator());
		auditEventDTO.setEventTypeCode(ae.getEventTypeCode());
		if(auditEventDTO.getAuditQueryParameters() != null){
			auditEventDTO.setAuditQueryParameters(ae.getAuditQueryParameters());
		}
		if(auditEventDTO.getAuditRequestDestination() != null){
			auditEventDTO.setAuditRequestDestination(ae.getAuditRequestDestination());
		}
		if(auditEventDTO.getAuditRequestSource() != null){
			auditEventDTO.setAuditRequestSource(ae.getAuditRequestSource());
		}
		if(auditEventDTO.getAuditSource() != null){
			auditEventDTO.setAuditSource(ae.getAuditSource());
		}
		if(auditEventDTO.getAuditHumanRequestors() != null){
			auditEventDTO.setAuditHumanRequestors(ae.getAuditHumanRequestors());
		}
		AuditEventDTO insertedAuditEventDTO = auditEventDao.createAuditEvent(auditEventDTO);
		return insertedAuditEventDTO;
	}

	@Override
	public List<AuditEventDTO> findAllAuditEvents() {
		return auditEventDao.findAllAuditEvents();
	}
}
