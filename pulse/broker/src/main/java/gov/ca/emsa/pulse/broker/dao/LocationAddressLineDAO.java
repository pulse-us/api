package gov.ca.emsa.pulse.broker.dao;

import java.util.List;
import gov.ca.emsa.pulse.broker.dto.AddressLineDTO;

public interface LocationAddressLineDAO {
	public AddressLineDTO create(AddressLineDTO dto, Long locationId, Integer lineOrder);
	public AddressLineDTO update(AddressLineDTO dto);
	public void delete(Long id);
	public void deleteAllForLocation(Long locationId);
	public List<AddressLineDTO> getByLocation(Long locationId);	
	public AddressLineDTO getByLocationAndLine(Long locationId, String line);
}
