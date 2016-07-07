package gov.ca.emsa.pulse.broker.manager.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gov.ca.emsa.pulse.broker.dao.AddressDAO;
import gov.ca.emsa.pulse.broker.dao.AlternateCareFacilityDAO;
import gov.ca.emsa.pulse.broker.dto.AlternateCareFacilityDTO;
import gov.ca.emsa.pulse.broker.manager.AlternateCareFacilityManager;

@Service
public class AlternateCareFacilityManagerImpl implements AlternateCareFacilityManager {
	@Autowired AlternateCareFacilityDAO acfDao;
	@Autowired AddressDAO addressDao;

	@Override
	public List<AlternateCareFacilityDTO> getAll() {
		return acfDao.findAll();
	}

	@Override
	@Transactional
	public AlternateCareFacilityDTO create(AlternateCareFacilityDTO toCreate) {
		return acfDao.create(toCreate);
	}

	@Override
	@Transactional
	public AlternateCareFacilityDTO update(AlternateCareFacilityDTO toUpdate) {
		toUpdate.setLastReadDate(new Date());
		AlternateCareFacilityDTO updated = acfDao.update(toUpdate);
		return updated;
	}

	@Override
	@Transactional
	public void cleanup(Date lastReadDate) {
		acfDao.deleteItemsOlderThan(lastReadDate);
	}

	@Override
	@Transactional
	public AlternateCareFacilityDTO getById(Long id) {
		AlternateCareFacilityDTO result = acfDao.getById(id);
		result.setLastReadDate(new Date());
		result = acfDao.update(result);
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
}
