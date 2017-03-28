package gov.ca.emsa.pulse.broker.dao;

import java.util.List;
import gov.ca.emsa.pulse.broker.dto.AddressLineDTO;

public interface AlternateCareFacilityAddressLineDAO {
	public AddressLineDTO create(AddressLineDTO dto, Long acfId, Integer lineOrder);
	public AddressLineDTO update(AddressLineDTO dto);
	public void delete(Long id);
	public void deleteAllForAcf(Long acfId);
	public List<AddressLineDTO> getByAcf(Long acfId);	
	public AddressLineDTO getByAcfAndLine(Long acfId, String line);
}
