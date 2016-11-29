package gov.ca.emsa.pulse.broker.dao;

import gov.ca.emsa.pulse.broker.dto.AuditEventDTO;
import gov.ca.emsa.pulse.broker.dto.AuditPatientDTO;

public interface AuditPatientDAO {
	
	public AuditPatientDTO createAuditPatient(AuditPatientDTO dto);

}
