package gov.ca.emsa.pulse.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import gov.ca.emsa.pulse.common.domain.OrganizationBase;
import gov.ca.emsa.pulse.broker.domain.QueryType;
import gov.ca.emsa.pulse.broker.dto.DtoToDomainConverter;
import gov.ca.emsa.pulse.broker.dto.OrganizationDTO;
import gov.ca.emsa.pulse.broker.manager.AuditManager;
import gov.ca.emsa.pulse.broker.manager.OrganizationManager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import gov.ca.emsa.pulse.auth.user.JWTAuthenticatedUser;

@Api(value = "organizations")
@RestController
@RequestMapping("/organizations")
public class OrganizationService {
	@Autowired OrganizationManager orgManager;
	@Autowired AuditManager auditManager;
	
	@ApiOperation(value="Get the list of organizations")
	@RequestMapping(value="", method=RequestMethod.GET)
    public List<OrganizationBase> getAll() {
		JWTAuthenticatedUser user = UserUtil.getCurrentUser();
		auditManager.addAuditEntry(QueryType.GET_ALL_ORGANIZATIONS, "/organizations", user.getUsername());
		List<OrganizationDTO> orgDtos = orgManager.getAll();
		List<OrganizationBase> orgs = new ArrayList<OrganizationBase>();
		for(OrganizationDTO orgDto : orgDtos) {
			DtoToDomainConverter.convert(orgDto);
		}
       return orgs;
    }
}
