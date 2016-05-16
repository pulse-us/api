package gov.ca.emsa.pulse.broker.manager.impl;

import java.util.Date;

import javax.persistence.EntityExistsException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
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
	public void updateOrganizations(Organization[] orgs){
		for(Organization org: orgs){
			OrganizationDTO orgdto = new OrganizationDTO();
			orgdto.setName(org.getName());
			orgdto.setId(org.getId());
			orgdto.setCreationDate(new Date());
			orgdto.setLastModifiedDate(new Date());
			try{
				organizationDAO.create(orgdto);
			}catch(EntityExistsException e){
				organizationDAO.update(orgdto);
			}
		}
	}

}
