package gov.ca.emsa.pulse.broker.dao;

import gov.ca.emsa.pulse.broker.dto.GivenNameDTO;

public interface GivenNameDAO {
	public GivenNameDTO create(GivenNameDTO dto);
	public GivenNameDTO update(GivenNameDTO dto);
	
	public void delete(Long id);
	public GivenNameDTO getById(Long id);
}
