package gov.ca.emsa.pulse.broker.dao;

import gov.ca.emsa.pulse.broker.dto.AuditEventDTO;
import gov.ca.emsa.pulse.broker.dto.AuditHumanRequestorDTO;

public interface AuditHumanRequestorDAO {
	
	public AuditHumanRequestorDTO createAuditHumanRequestor(AuditHumanRequestorDTO dto);
	
}
