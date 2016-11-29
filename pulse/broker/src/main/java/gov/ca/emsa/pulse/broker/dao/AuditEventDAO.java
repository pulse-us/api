package gov.ca.emsa.pulse.broker.dao;

import gov.ca.emsa.pulse.broker.dto.AuditEventDTO;
import java.util.List;

public interface AuditEventDAO {
	public AuditEventDTO createAuditEvent(AuditEventDTO dto);
	public AuditEventDTO getAuditEventById(Long id);
	public List<AuditEventDTO> findAllAuditEvents();
}
