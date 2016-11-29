package gov.ca.emsa.pulse.broker.manager;

import gov.ca.emsa.pulse.broker.audit.AuditEvent;
import gov.ca.emsa.pulse.broker.domain.QueryType;
import gov.ca.emsa.pulse.broker.dto.AuditEventDTO;

import java.util.List;

public interface AuditEventManager {
	public AuditEventDTO addAuditEventEntryIG(AuditEventDTO ae);
	public List<AuditEventDTO> findAllAuditEvents();
}
