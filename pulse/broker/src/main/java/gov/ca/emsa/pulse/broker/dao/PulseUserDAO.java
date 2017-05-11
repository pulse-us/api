package gov.ca.emsa.pulse.broker.dao;

import gov.ca.emsa.pulse.broker.dto.PulseUserDTO;

public interface PulseUserDAO {
	public PulseUserDTO create(PulseUserDTO dto);
	public PulseUserDTO update(PulseUserDTO dto);
	
	public void delete(Long id);
	public PulseUserDTO getById(Long id);
}
