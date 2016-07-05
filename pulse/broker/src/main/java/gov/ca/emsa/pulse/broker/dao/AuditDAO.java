package gov.ca.emsa.pulse.broker.dao;

import java.util.List;

import gov.ca.emsa.pulse.broker.dto.AuditDTO;

public interface AuditDAO {
	public AuditDTO create(AuditDTO dto);
	AuditDTO getById(Long id);
	List<AuditDTO> findAll();
	AuditDTO getByQuerent(String querent);
}
