package gov.ca.emsa.pulse.broker.dao;

import java.util.List;

import gov.ca.emsa.pulse.broker.dto.AddressDTO;

public interface AddressDAO {
	public AddressDTO create(AddressDTO dto);
	public AddressDTO update(AddressDTO dto);
	public void delete(Long id);
	public List<AddressDTO> findAll();	
	public AddressDTO getById(Long id);
	public AddressDTO getByValues(AddressDTO address);
}
