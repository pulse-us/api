package gov.ca.emsa.pulse.broker.dao;

import gov.ca.emsa.pulse.broker.dto.AuditEventDTO;
import gov.ca.emsa.pulse.broker.dto.AuditRequestDestinationDTO;

public interface AuditRequestDestinationDAO {
	
	public AuditRequestDestinationDTO createAuditRequestDestination(AuditRequestDestinationDTO dto);

}
