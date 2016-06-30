package gov.ca.emsa.pulse.broker.manager;

import java.util.Date;
import java.util.List;

import gov.ca.emsa.pulse.broker.dto.AlternateCareFacilityDTO;

public interface AlternateCareFacilityManager {
	public List<AlternateCareFacilityDTO> getAll();
	public AlternateCareFacilityDTO getById(Long id);
	public AlternateCareFacilityDTO getByName(String name);
	public AlternateCareFacilityDTO create(AlternateCareFacilityDTO toCreate);
	public AlternateCareFacilityDTO update(AlternateCareFacilityDTO toUpdate);
	public void cleanup(Date lastReadDate);
}
