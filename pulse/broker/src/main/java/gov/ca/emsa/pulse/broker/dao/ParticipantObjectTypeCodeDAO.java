package gov.ca.emsa.pulse.broker.dao;

import gov.ca.emsa.pulse.broker.dto.EventActionCodeDTO;
import gov.ca.emsa.pulse.broker.dto.NameRepresentationDTO;
import gov.ca.emsa.pulse.broker.dto.NetworkAccessPointTypeCodeDTO;
import gov.ca.emsa.pulse.broker.dto.ParticipantObjectTypeCodeDTO;

public interface ParticipantObjectTypeCodeDAO {
	public ParticipantObjectTypeCodeDTO create(ParticipantObjectTypeCodeDTO dto);
	public ParticipantObjectTypeCodeDTO update(ParticipantObjectTypeCodeDTO dto);
	
	public void delete(Long id);
	public ParticipantObjectTypeCodeDTO getById(Long id);
	public ParticipantObjectTypeCodeDTO getByCode(String code);
}
