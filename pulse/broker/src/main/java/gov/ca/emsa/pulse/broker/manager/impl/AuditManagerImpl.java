package gov.ca.emsa.pulse.broker.manager.impl;

import gov.ca.emsa.pulse.broker.audit.AuditEvent;
import gov.ca.emsa.pulse.broker.dao.AuditDAO;
import gov.ca.emsa.pulse.broker.domain.QueryType;
import gov.ca.emsa.pulse.broker.dto.AuditEventDTO;
import gov.ca.emsa.pulse.broker.manager.AuditManager;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuditManagerImpl implements AuditManager{
	
	@Autowired
	private AuditDAO auditDao;
	
	@Override
	@Transactional
	public AuditEventDTO addAuditEntry(AuditEvent ae){
		AuditEventDTO audit = new AuditEventDTO();
		audit.setEventId(ae.getEventId());
		audit.setEventActionCode(ae.getEventActionCode());
		audit.setEventDateTime(ae.getEventDateTime());
		audit.setEventOutcomeIndicator(ae.getEventOutcomeIndicator());
		audit.setEventTypeCode(ae.getEventTypeCode());
		audit.setAuditRequestSourceId(ae.getAuditSourceId());
		audit.setAuditRequestDestinationId(ae.getAuditRequestDestinationId());
		audit.setAuditSourceId(ae.getAuditSourceId());
		audit.setAuditQueryParametersId(ae.getAuditQueryParametersId());
		AuditEventDTO aud = auditDao.createAuditEvent(audit);
		return aud;
	}
	
	@Override
	@Transactional
	public List<AuditEventDTO> findAllAuditEvents(){
		List<AuditEventDTO> audits = auditDao.findAllAuditEvents();
		return audits;
	}

}
