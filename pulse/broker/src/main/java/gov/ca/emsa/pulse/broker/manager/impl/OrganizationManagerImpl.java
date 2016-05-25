package gov.ca.emsa.pulse.broker.manager.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityExistsException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gov.ca.emsa.pulse.broker.dao.OrganizationDAO;
import gov.ca.emsa.pulse.broker.domain.Organization;
import gov.ca.emsa.pulse.broker.dto.OrganizationDTO;
import gov.ca.emsa.pulse.broker.manager.OrganizationManager;

@Service
public class OrganizationManagerImpl implements OrganizationManager {
	
	@Autowired
	private OrganizationDAO organizationDAO;
	
	@Transactional
	public void updateOrganizations(ArrayList<Organization> orgs){
		List<OrganizationDTO> currentOrgs = getAll();
		HashMap<Long,OrganizationDTO> currentOrgsHash = new HashMap<Long,OrganizationDTO>();
		for(OrganizationDTO currentOrg : currentOrgs){
			currentOrgsHash.put(currentOrg.getId(), currentOrg);
		}
		List<Long> currentOrgIds = new ArrayList<Long>();
		List<Long> updatedOrgIds = new ArrayList<Long>();
		for(OrganizationDTO orgdto : currentOrgs){
			currentOrgIds.add(orgdto.getId());
		}
		for(Organization org: orgs){
			updatedOrgIds.add(org.getId());
		}
		for(Long currentId : currentOrgIds){
			if(!updatedOrgIds.contains(currentId)){
				organizationDAO.delete(currentOrgsHash.get(currentId));
			}
		}
		for(Organization org: orgs){
			OrganizationDTO orgdto = new OrganizationDTO();
			orgdto.setName(org.getName());
			orgdto.setId(org.getId());
			orgdto.setActive(org.isActive());
			orgdto.setAdapter(org.getAdapter());
			orgdto.setIpAddress(org.getIpAddress());
			if(org.getUsername() != null){
				orgdto.setUsername(org.getUsername());
			}
			if(org.getPassword() != null){
				orgdto.setPassword(org.getPassword());
			}
			if(org.getCertificationKey() != null){
				orgdto.setCertificationKey(org.getCertificationKey());
			}
			if(org.getEndpointUrl() != null){
				orgdto.setEndpointUrl(org.getEndpointUrl());
			}
			orgdto.setCreationDate(new Date());
			orgdto.setLastModifiedDate(new Date());
			try{
				organizationDAO.create(orgdto);
			}catch(EntityExistsException e){
				organizationDAO.update(orgdto);
			}
		}
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<OrganizationDTO> getAll() {
		List<OrganizationDTO> allOrganizations = organizationDAO.findAll();
		return allOrganizations;
	}

}
