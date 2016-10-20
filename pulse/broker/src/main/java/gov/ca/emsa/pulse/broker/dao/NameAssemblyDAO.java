package gov.ca.emsa.pulse.broker.dao;

import gov.ca.emsa.pulse.broker.dto.NameAssemblyDTO;

public interface NameAssemblyDAO {
	public NameAssemblyDTO create(NameAssemblyDTO dto);
	public NameAssemblyDTO update(NameAssemblyDTO dto);
	
	public void delete(Long id);
	public NameAssemblyDTO getById(Long id);
}
