package gov.ca.emsa.pulse.broker.manager;

import gov.ca.emsa.pulse.broker.dto.AuditDTO;

public interface AuditManager {
	public void addAuditEntry(AuditDTO audit);
}
