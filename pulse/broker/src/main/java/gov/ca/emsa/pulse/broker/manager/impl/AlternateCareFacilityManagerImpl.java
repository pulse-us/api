package gov.ca.emsa.pulse.broker.manager.impl;

import gov.ca.emsa.pulse.broker.cache.CacheCleanupException;
import gov.ca.emsa.pulse.broker.dao.AlternateCareFacilityDAO;
import gov.ca.emsa.pulse.broker.dto.AlternateCareFacilityDTO;
import gov.ca.emsa.pulse.broker.manager.AlternateCareFacilityManager;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityExistsException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AlternateCareFacilityManagerImpl implements AlternateCareFacilityManager {
	@Value("${acfWritesAllowed}")
	private Boolean acfWritesAllowed;
	
	@Autowired AlternateCareFacilityDAO acfDao;

	@Override
	public List<AlternateCareFacilityDTO> getAll() {
		return acfDao.findAll();
	}

	@Override
	@Transactional
	public AlternateCareFacilityDTO create(AlternateCareFacilityDTO toCreate) throws SQLException {
		AlternateCareFacilityDTO result = null;
		
		try {
			result = acfDao.create(toCreate);
		} catch(EntityExistsException ex) {
			result = getByName(toCreate.getName());
		}
		
		return result;
	}

	@Override
	@Transactional
	public AlternateCareFacilityDTO update(AlternateCareFacilityDTO toUpdate) throws SQLException {
		toUpdate.setLastReadDate(new Date());
		AlternateCareFacilityDTO updated = acfDao.update(toUpdate);
		return updated;
	}

	@Override
	@Transactional
	public AlternateCareFacilityDTO getById(Long id) {
		AlternateCareFacilityDTO result = acfDao.getById(id);
		return result;
	}

	@Override
	public AlternateCareFacilityDTO getByName(String name) {
		List<AlternateCareFacilityDTO> matches = acfDao.getByName(name);
		
		AlternateCareFacilityDTO result = null;
		if(matches != null && matches.size() > 0) {
			result = matches.get(0);
		}
		return result;
	}
	
	@Override
	@Transactional
	public void cleanupCache(Date oldestAllowed) throws CacheCleanupException {
		if(acfWritesAllowed != null && acfWritesAllowed.booleanValue() == true) {
			try {
				acfDao.deleteItemsOlderThan(oldestAllowed);
			} catch(SQLException sql) {
				throw new CacheCleanupException("Error updating the database. Message is: " + sql.getMessage());
			} catch(Exception ex) {
				throw new CacheCleanupException(ex.getMessage());
			}
		}
	}
}
