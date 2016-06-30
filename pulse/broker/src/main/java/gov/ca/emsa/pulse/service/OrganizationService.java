package gov.ca.emsa.pulse.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import gov.ca.emsa.pulse.broker.domain.OrganizationBase;
import gov.ca.emsa.pulse.broker.dto.OrganizationDTO;
import gov.ca.emsa.pulse.broker.manager.OrganizationManager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "organizations")
@RestController
@RequestMapping("/organizations")
public class OrganizationService {
	@Autowired OrganizationManager orgManager;
	
	@ApiOperation(value="Get the list of organizations")
	@RequestMapping(value="", method=RequestMethod.GET)
    public List<OrganizationBase> getAll() {
		List<OrganizationDTO> orgDtos = orgManager.getAll();
		List<OrganizationBase> orgs = new ArrayList<OrganizationBase>();
		for(OrganizationDTO orgDto : orgDtos) {
			OrganizationBase org = new OrganizationBase(orgDto);
			orgs.add(org);
		}
       return orgs;
    }
}
