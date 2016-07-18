package gov.ca.emsa.pulse.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import gov.ca.emsa.pulse.common.domain.Address;
import gov.ca.emsa.pulse.common.domain.AlternateCareFacility;
import gov.ca.emsa.pulse.common.domain.CommonUser;
import gov.ca.emsa.pulse.broker.domain.Audit;
import gov.ca.emsa.pulse.broker.domain.QueryType;
import gov.ca.emsa.pulse.broker.dto.AddressDTO;
import gov.ca.emsa.pulse.broker.dto.AlternateCareFacilityDTO;
import gov.ca.emsa.pulse.broker.dto.DtoToDomainConverter;
import gov.ca.emsa.pulse.broker.manager.AlternateCareFacilityManager;
import gov.ca.emsa.pulse.broker.manager.AuditManager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "acfs")
@RestController
@RequestMapping("/acfs")
public class AlternateCareFacilityService {
	@Autowired AlternateCareFacilityManager acfManager;
	@Autowired AuditManager auditManager;

	@ApiOperation(value="Get the list of all alternate care facilities (ACFs)")
	@RequestMapping(value="",  method = RequestMethod.GET)
    public List<AlternateCareFacility> getAll() {
		CommonUser user = UserUtil.getCurrentUser();
		auditManager.addAuditEntry(QueryType.GET_ALL_ACFS, "/acfs", user.getEmail());
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
		auditManager.addAuditEntry(QueryType.GET_ACF_BY_ID, "/acfs" + acfId, user.getEmail());
		AlternateCareFacilityDTO dto = acfManager.getById(acfId);
		return DtoToDomainConverter.convert(dto);
    }

	@ApiOperation(value = "Create a new ACF")
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public AlternateCareFacility create(@RequestBody(required=true) AlternateCareFacility toCreate) {
		CommonUser user = UserUtil.getCurrentUser();
		auditManager.addAuditEntry(QueryType.CREATE_ACF, "/create", user.getEmail());
		AlternateCareFacilityDTO dto = new AlternateCareFacilityDTO();
		dto.setName(toCreate.getName());
		dto.setPhoneNumber(toCreate.getPhoneNumber());
		if(toCreate.getAddress() != null) {
			AddressDTO address = new AddressDTO();
			address.setId(toCreate.getAddress().getId());
			address.setStreetLineOne(toCreate.getAddress().getStreet1());
			address.setStreetLineTwo(toCreate.getAddress().getStreet2());
			address.setCity(toCreate.getAddress().getCity());
			address.setState(toCreate.getAddress().getState());
			address.setZipcode(toCreate.getAddress().getZipcode());
			address.setCountry(toCreate.getAddress().getCountry());
			dto.setAddress(address);
		}
		AlternateCareFacilityDTO created = acfManager.create(dto);
		return DtoToDomainConverter.convert(created);

	}

	@ApiOperation(value = "Edit an existing ACF")
	@RequestMapping(value = "/{acfId}/edit", method = RequestMethod.POST)
	public AlternateCareFacility update(@RequestBody(required=true) AlternateCareFacility toUpdate)
		throws Exception {
		CommonUser user = UserUtil.getCurrentUser();
		auditManager.addAuditEntry(QueryType.EDIT_ACF, user.getAcf() + "/edit", user.getEmail());
		AlternateCareFacilityDTO userAcf = acfManager.getByName(user.getAcf());
		if(userAcf == null) {
			throw new Exception("The current user's ACF (" + user.getAcf() + ") was not found in the database.");
		}
		if(!userAcf.getId().equals(toUpdate.getId())) {
			throw new Exception("User " + user.getSubjectName() + " does not have permission to edit ACF " + toUpdate.getName());
		}

		AlternateCareFacilityDTO dto = acfManager.getById(toUpdate.getId());
		dto.setName(toUpdate.getName());
		dto.setPhoneNumber(toUpdate.getPhoneNumber());
		if(toUpdate.getAddress() != null) {
			AddressDTO address = new AddressDTO();
			address.setId(toUpdate.getAddress().getId());
			address.setStreetLineOne(toUpdate.getAddress().getStreet1());
			address.setStreetLineTwo(toUpdate.getAddress().getStreet2());
			address.setCity(toUpdate.getAddress().getCity());
			address.setState(toUpdate.getAddress().getState());
			address.setZipcode(toUpdate.getAddress().getZipcode());
			address.setCountry(toUpdate.getAddress().getCountry());
			dto.setAddress(address);
		} else {
			dto.setAddress(null);
		}
		AlternateCareFacilityDTO updated = acfManager.update(dto);
		return DtoToDomainConverter.convert(updated);
	}
}
