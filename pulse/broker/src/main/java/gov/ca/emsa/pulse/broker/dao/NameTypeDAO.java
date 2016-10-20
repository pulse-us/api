package gov.ca.emsa.pulse.broker.dao;

import gov.ca.emsa.pulse.broker.dto.NameTypeDTO;

public interface NameTypeDAO {
	public NameTypeDTO create(NameTypeDTO dto);
	public NameTypeDTO update(NameTypeDTO dto);
	
	public void delete(Long id);
	public NameTypeDTO getById(Long id);
}
