package gov.ca.emsa.pulse.broker.dao;

import gov.ca.emsa.pulse.broker.dto.EventActionCodeDTO;
import gov.ca.emsa.pulse.broker.dto.NameRepresentationDTO;
import gov.ca.emsa.pulse.broker.dto.PulseEventActionCodeDTO;
import gov.ca.emsa.pulse.broker.dto.PulseEventActionDTO;

public interface PulseEventActionDAO {
	public PulseEventActionDTO create(PulseEventActionDTO dto);
	
	public void delete(Long id);
	public PulseEventActionDTO getById(Long id);
	public PulseEventActionDTO getByCode(String code);
}
