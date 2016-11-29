package gov.ca.emsa.pulse.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import gov.ca.emsa.pulse.auth.user.CommonUser;
import gov.ca.emsa.pulse.broker.domain.QueryType;
import gov.ca.emsa.pulse.broker.dto.AlternateCareFacilityDTO;
import gov.ca.emsa.pulse.broker.dto.DomainToDtoConverter;
import gov.ca.emsa.pulse.broker.dto.DtoToDomainConverter;
import gov.ca.emsa.pulse.broker.manager.AlternateCareFacilityManager;
import gov.ca.emsa.pulse.broker.manager.AuditEventManager;
import gov.ca.emsa.pulse.common.domain.AlternateCareFacility;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "acfs")
@RestController
@RequestMapping("/acfs")
public class AlternateCareFacilityService {
	@Autowired AlternateCareFacilityManager acfManager;
	@Autowired AuditEventManager auditManager;

	@ApiOperation(value="Get the list of all alternate care facilities (ACFs)")
	@RequestMapping(value="",  method = RequestMethod.GET)
    public List<AlternateCareFacility> getAll() {
		CommonUser user = UserUtil.getCurrentUser();
		//auditManager.addAuditEntry(QueryType.GET_ALL_ACFS, "/acfs", user.getSubjectName());
		List<AlternateCareFacilityDTO> dtos = acfManager.getAll();
		List<AlternateCareFacility> results = new ArrayList<AlternateCareFacility>();
		for(AlternateCareFacilityDTO dto : dtos) {
			results.add(DtoToDomainConverter.convert(dto));
		}
       return results;
    }

	@ApiOperation(value="Get information about a specific ACF")
	@RequestMapping(value = "/{acfId}", method=RequestMethod.GET)
    public AlternateCareFacility getById(@PathVariable("acfId") Long acfId) {
		CommonUser user = UserUtil.getCurrentUser();
		//auditManager.addAuditEntry(QueryType.GET_ACF_BY_ID, "/acfs" + acfId, user.getSubjectName());
		AlternateCareFacilityDTO dto = acfManager.getById(acfId);
		return DtoToDomainConverter.convert(dto);
    }

	@ApiOperation(value = "Create a new ACF")
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public AlternateCareFacility create(@RequestBody(required=true) AlternateCareFacility toCreate) 
		throws InvalidArgumentsException, SQLException {
		CommonUser user = UserUtil.getCurrentUser();
		//auditManager.addAuditEntry(QueryType.CREATE_ACF, "/create", user.getSubjectName());
		AlternateCareFacilityDTO dto = DomainToDtoConverter.convert(toCreate);
		if(StringUtils.isEmpty(dto.getName())) {
			throw new InvalidArgumentsException("ACF name is required.");
		}
		AlternateCareFacilityDTO created = acfManager.create(dto);
		return DtoToDomainConverter.convert(created);

	}

	@ApiOperation(value = "Edit an existing ACF")
	@RequestMapping(value = "/{acfId}/edit", method = RequestMethod.POST)
	public AlternateCareFacility update(@RequestBody(required=true) AlternateCareFacility toUpdate)
		throws InvalidArgumentsException, PermissionDeniedException, SQLException {
		CommonUser user = UserUtil.getCurrentUser();
		//auditManager.addAuditEntry(QueryType.EDIT_ACF, user.getAcf() + "/edit", user.getSubjectName());
		if(user.getAcf() == null) {
			throw new InvalidArgumentsException("No ACF was found in the User header.");
		}
		if(!user.getAcf().getId().equals(toUpdate.getId())) {
			throw new PermissionDeniedException("User " + user.getSubjectName() + " does not have permission to edit ACF " + toUpdate.getName());
		}

		AlternateCareFacilityDTO dto = DomainToDtoConverter.convert(toUpdate);
		
		if(dto.getId() == null) {
			throw new InvalidArgumentsException("An ACF id is required in the body of the update request.");
		}
		AlternateCareFacilityDTO updated = acfManager.update(dto);
		return DtoToDomainConverter.convert(updated);
	}
}
