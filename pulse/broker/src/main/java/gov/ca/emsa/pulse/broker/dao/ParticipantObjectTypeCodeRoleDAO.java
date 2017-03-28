package gov.ca.emsa.pulse.broker.dao;

import gov.ca.emsa.pulse.broker.dto.EventActionCodeDTO;
import gov.ca.emsa.pulse.broker.dto.NameRepresentationDTO;
import gov.ca.emsa.pulse.broker.dto.NetworkAccessPointTypeCodeDTO;
import gov.ca.emsa.pulse.broker.dto.ParticipantObjectTypeCodeDTO;
import gov.ca.emsa.pulse.broker.dto.ParticipantObjectTypeCodeRoleDTO;

public interface ParticipantObjectTypeCodeRoleDAO {
	public ParticipantObjectTypeCodeRoleDTO create(ParticipantObjectTypeCodeRoleDTO dto);
	public ParticipantObjectTypeCodeRoleDTO update(ParticipantObjectTypeCodeRoleDTO dto);
	
	public void delete(Long id);
	public ParticipantObjectTypeCodeRoleDTO getById(Long id);
	public ParticipantObjectTypeCodeRoleDTO getByCode(String code);
}
