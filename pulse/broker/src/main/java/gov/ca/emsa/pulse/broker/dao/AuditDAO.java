package gov.ca.emsa.pulse.broker.dao;

import gov.ca.emsa.pulse.broker.dto.AuditEventDTO;
import gov.ca.emsa.pulse.broker.entity.AuditEventEntity;

import java.util.List;

public interface AuditDAO {
	public AuditEventDTO createAuditEvent(AuditEventDTO dto);
	public AuditEventDTO getAuditEventById(Long id);
	public List<AuditEventDTO> findAllAuditEvents();
}
