package gov.ca.emsa.pulse.broker.manager.impl;

import gov.ca.emsa.pulse.broker.dao.PulseUserDAO;
import gov.ca.emsa.pulse.broker.dto.AlternateCareFacilityDTO;
import gov.ca.emsa.pulse.broker.dto.PulseUserDTO;
import gov.ca.emsa.pulse.broker.manager.PulseUserManager;

import java.sql.SQLException;

import javax.persistence.EntityExistsException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PulseUserManagerImpl implements PulseUserManager{
	
	private static final Logger logger = LogManager.getLogger(AlternateCareFacilityManagerImpl.class);
	
	@Autowired private PulseUserDAO pulseUserDao;
	
	@Override
	@Transactional
	public PulseUserDTO create(PulseUserDTO toCreate){
		PulseUserDTO result = null;
		
		result = pulseUserDao.create(toCreate);
		
		return result;
	}
	
	@Override
	@Transactional
	public PulseUserDTO getById(Long id) {
		PulseUserDTO result = pulseUserDao.getById(id);
		return result;
	}

}
