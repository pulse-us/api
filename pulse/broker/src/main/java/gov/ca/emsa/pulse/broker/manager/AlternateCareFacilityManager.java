package gov.ca.emsa.pulse.broker.manager;

import gov.ca.emsa.pulse.broker.dto.AlternateCareFacilityDTO;

import java.util.List;

public interface AlternateCareFacilityManager extends CachedDataManager {
	public List<AlternateCareFacilityDTO> getAll();
	public AlternateCareFacilityDTO getById(Long id);
	public AlternateCareFacilityDTO getByName(String name);
	public AlternateCareFacilityDTO create(AlternateCareFacilityDTO toCreate);
	public AlternateCareFacilityDTO update(AlternateCareFacilityDTO toUpdate);
}
