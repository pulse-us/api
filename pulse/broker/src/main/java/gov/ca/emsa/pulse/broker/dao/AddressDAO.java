package gov.ca.emsa.pulse.broker.dao;

import gov.ca.emsa.pulse.broker.dto.AddressDTO;

import java.util.List;

public interface AddressDAO {
	public AddressDTO create(AddressDTO dto);
	public AddressDTO update(AddressDTO dto);
	public void delete(Long id);
	public List<AddressDTO> findAll();	
	public AddressDTO getById(Long id);
	public AddressDTO getByValues(AddressDTO address);
}
