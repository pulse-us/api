package gov.ca.emsa.pulse.broker.dao;

import gov.ca.emsa.pulse.broker.dto.PulseEventActionDTO;

public interface PulseEventActionDAO {
	public PulseEventActionDTO create(PulseEventActionDTO dto);
	
	public void delete(Long id);
	public PulseEventActionDTO getById(Long id);
}
