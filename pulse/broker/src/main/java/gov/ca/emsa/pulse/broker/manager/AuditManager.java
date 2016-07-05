package gov.ca.emsa.pulse.broker.manager;

import java.util.List;

import gov.ca.emsa.pulse.broker.domain.QueryType;
import gov.ca.emsa.pulse.broker.dto.AuditDTO;

public interface AuditManager {
	public AuditDTO addAuditEntry(QueryType queryType, String query, String querent);
	public List<AuditDTO> getAll();
}
