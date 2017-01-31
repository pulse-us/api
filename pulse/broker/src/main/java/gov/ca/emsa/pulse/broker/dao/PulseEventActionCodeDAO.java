package gov.ca.emsa.pulse.broker.dao;

import gov.ca.emsa.pulse.broker.dto.EventActionCodeDTO;
import gov.ca.emsa.pulse.broker.dto.NameRepresentationDTO;
import gov.ca.emsa.pulse.broker.dto.PulseEventActionCodeDTO;

public interface PulseEventActionCodeDAO {
	public PulseEventActionCodeDTO create(PulseEventActionCodeDTO dto);
	public PulseEventActionCodeDTO update(PulseEventActionCodeDTO dto);
	
	public void delete(Long id);
	public PulseEventActionCodeDTO getById(Long id);
	public PulseEventActionCodeDTO getByCode(String code);
}
