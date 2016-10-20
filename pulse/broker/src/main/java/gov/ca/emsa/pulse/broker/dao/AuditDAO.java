package gov.ca.emsa.pulse.broker.dao;

import gov.ca.emsa.pulse.broker.dto.AuditDTO;

import java.util.List;

public interface AuditDAO {
	public AuditDTO create(AuditDTO dto);
	AuditDTO getById(Long id);
	List<AuditDTO> findAll();
	AuditDTO getByQuerent(String querent);
}
