package gov.ca.emsa.pulse.broker.manager;

import gov.ca.emsa.pulse.broker.dto.PulseUserDTO;

import java.util.List;

public interface PulseUserManager {
	public PulseUserDTO getById(Long id);
	public PulseUserDTO create(PulseUserDTO pulseUser);
}
