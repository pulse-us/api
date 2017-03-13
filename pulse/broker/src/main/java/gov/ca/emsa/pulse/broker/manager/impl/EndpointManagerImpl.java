package gov.ca.emsa.pulse.broker.manager.impl;

import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gov.ca.emsa.pulse.broker.dao.EndpointDAO;
import gov.ca.emsa.pulse.broker.dto.DomainToDtoConverter;
import gov.ca.emsa.pulse.broker.dto.EndpointDTO;
import gov.ca.emsa.pulse.broker.manager.EndpointManager;
import gov.ca.emsa.pulse.common.domain.Endpoint;

@Service
public class EndpointManagerImpl implements EndpointManager {
	private static final Logger logger = LogManager.getLogger(EndpointManagerImpl.class);

	@Autowired private EndpointDAO endpointDao;
	
	@Transactional(readOnly = true)
	public EndpointDTO getById(Long id) {
		return endpointDao.findById(id);
	}
	
	@Transactional
	public void updateEndpoints(List<Endpoint> newEndpoints){
		List<EndpointDTO> currentEndpoints = getAll();
		
		//look at the old endpoints for anything not in the new endpoints and delete
		for(EndpointDTO oldEndpoint : currentEndpoints) {
			boolean stillExists = false;
			for(Endpoint newEndpoint : newEndpoints) {
				if(oldEndpoint.getExternalId().equals(newEndpoint.getExternalId())) {
					stillExists = true;
				}
			}
			
			if(!stillExists) {
				endpointDao.delete(oldEndpoint);
			}
		}
		
		//look at the new endpoints for anything that should be created or updated
		for(Endpoint newEndpoint : newEndpoints) {
			String externalId = newEndpoint.getExternalId();
			EndpointDTO existingEndpoint = endpointDao.findByExternalId(externalId);
			if(existingEndpoint == null) {
				try {
					EndpointDTO toCreate = DomainToDtoConverter.convert(newEndpoint);
					endpointDao.create(toCreate);
				} catch(Exception ex) {
					logger.error("Error creating endpoint with external id " + externalId, ex);
					ex.printStackTrace();
				}
			} else {
				try {
					EndpointDTO toUpdate = DomainToDtoConverter.convert(newEndpoint);
					toUpdate.setId(existingEndpoint.getId());
					endpointDao.update(toUpdate);
				} catch(Exception ex) {
					logger.error("Error updating endpoint with id " + existingEndpoint.getId(), ex);
					ex.printStackTrace();
				}
			}
		}
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<EndpointDTO> getAll() {
		List<EndpointDTO> results = endpointDao.findAll();
		return results;
	}
}
