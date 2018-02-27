package gov.ca.emsa.pulse.broker.manager;

import gov.ca.emsa.pulse.broker.dto.AlternateCareFacilityDTO;

import java.sql.SQLException;
import java.util.List;

public interface AlternateCareFacilityManager extends CachedDataManager {
	public List<AlternateCareFacilityDTO> getAll();
	public AlternateCareFacilityDTO getById(Long id);
	public AlternateCareFacilityDTO getByIdentifier(String identifier);
	public AlternateCareFacilityDTO getByLiferayAcfId(Long liferayAcfId);
	public List<AlternateCareFacilityDTO> getByName(String name);
	public AlternateCareFacilityDTO create(AlternateCareFacilityDTO toCreate) throws SQLException;
	public AlternateCareFacilityDTO updateLastModifiedDate(Long acfId) throws SQLException;
	public AlternateCareFacilityDTO updateAcfDetails(AlternateCareFacilityDTO toUpdate) throws SQLException;
}
