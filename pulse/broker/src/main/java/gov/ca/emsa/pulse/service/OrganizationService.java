package gov.ca.emsa.pulse.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import gov.ca.emsa.pulse.common.domain.Organization;
import gov.ca.emsa.pulse.common.domain.OrganizationBase;
import gov.ca.emsa.pulse.auth.user.CommonUser;
import gov.ca.emsa.pulse.broker.domain.QueryType;
import gov.ca.emsa.pulse.broker.dto.DtoToDomainConverter;
import gov.ca.emsa.pulse.broker.dto.OrganizationDTO;
import gov.ca.emsa.pulse.broker.manager.AuditManager;
import gov.ca.emsa.pulse.broker.manager.OrganizationManager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "organizations")
@RestController
@RequestMapping("/organizations")
public class OrganizationService {
	@Autowired OrganizationManager orgManager;
	@Autowired AuditManager auditManager;

	@ApiOperation(value="Get the list of organizations")
	@RequestMapping(value="", method=RequestMethod.GET)
    public List<OrganizationBase> getAll() {
		CommonUser user = UserUtil.getCurrentUser();
		auditManager.addAuditEntry(QueryType.GET_ALL_ORGANIZATIONS, "/organizations", user.getSubjectName());
		List<OrganizationDTO> orgDtos = orgManager.getAll();
		List<OrganizationBase> orgs = new ArrayList<OrganizationBase>();
		for(OrganizationDTO orgDto : orgDtos) {
			Organization org = DtoToDomainConverter.convert(orgDto);
			orgs.add(org);
		}
       return orgs;
    }
}
