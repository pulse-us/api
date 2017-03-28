package gov.ca.emsa.pulse.broker.dao;

import gov.ca.emsa.pulse.broker.dto.NameRepresentationDTO;

public interface NameRepresentationDAO {
	public NameRepresentationDTO create(NameRepresentationDTO dto);
	public NameRepresentationDTO update(NameRepresentationDTO dto);
	
	public void delete(Long id);
	public NameRepresentationDTO getById(Long id);
	public NameRepresentationDTO getByCode(String code);
}
