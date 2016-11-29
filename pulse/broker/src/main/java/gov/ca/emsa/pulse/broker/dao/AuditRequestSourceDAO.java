package gov.ca.emsa.pulse.broker.dao;

import gov.ca.emsa.pulse.broker.dto.AuditEventDTO;
import gov.ca.emsa.pulse.broker.dto.AuditRequestSourceDTO;

public interface AuditRequestSourceDAO {
	
	public AuditRequestSourceDTO createAuditRequestSource(AuditRequestSourceDTO dto);

}
