package gov.ca.emsa.pulse.broker.dao;

import gov.ca.emsa.pulse.broker.dto.EventActionCodeDTO;
import gov.ca.emsa.pulse.broker.dto.NameRepresentationDTO;
import gov.ca.emsa.pulse.broker.dto.NetworkAccessPointTypeCodeDTO;

public interface NetworkAccessPointTypeCodeDAO {
	public NetworkAccessPointTypeCodeDTO create(NetworkAccessPointTypeCodeDTO dto);
	public NetworkAccessPointTypeCodeDTO update(NetworkAccessPointTypeCodeDTO dto);
	
	public void delete(Long id);
	public NetworkAccessPointTypeCodeDTO getById(Long id);
	public NetworkAccessPointTypeCodeDTO getByCode(String code);
}
