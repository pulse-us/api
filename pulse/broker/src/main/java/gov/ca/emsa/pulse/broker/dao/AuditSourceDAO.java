package gov.ca.emsa.pulse.broker.dao;

import gov.ca.emsa.pulse.broker.dto.AuditEventDTO;
import gov.ca.emsa.pulse.broker.dto.AuditSourceDTO;

public interface AuditSourceDAO {
	
	public AuditSourceDTO createAuditSource(AuditSourceDTO dto);
	
}
