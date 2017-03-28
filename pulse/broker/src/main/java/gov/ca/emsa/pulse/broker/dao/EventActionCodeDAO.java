package gov.ca.emsa.pulse.broker.dao;

import gov.ca.emsa.pulse.broker.dto.EventActionCodeDTO;
import gov.ca.emsa.pulse.broker.dto.NameRepresentationDTO;

public interface EventActionCodeDAO {
	public EventActionCodeDTO create(EventActionCodeDTO dto);
	public EventActionCodeDTO update(EventActionCodeDTO dto);
	
	public void delete(Long id);
	public EventActionCodeDTO getById(Long id);
	public EventActionCodeDTO getByCode(String code);
}
