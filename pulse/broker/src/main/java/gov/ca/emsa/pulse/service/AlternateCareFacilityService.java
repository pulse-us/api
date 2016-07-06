package gov.ca.emsa.pulse.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import gov.ca.emsa.pulse.broker.domain.AlternateCareFacility;
import gov.ca.emsa.pulse.broker.domain.User;
import gov.ca.emsa.pulse.broker.dto.AddressDTO;
import gov.ca.emsa.pulse.broker.dto.AlternateCareFacilityDTO;
import gov.ca.emsa.pulse.broker.manager.AlternateCareFacilityManager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "acfs")
@RestController
@RequestMapping("/acfs")
public class AlternateCareFacilityService {
	@Autowired AlternateCareFacilityManager acfManager;
	
	@ApiOperation(value="Get the list of all alternate care facilities (ACFs)")
	@RequestMapping(value="",  method = RequestMethod.GET)
    public List<AlternateCareFacility> getAll() {
		List<AlternateCareFacilityDTO> dtos = acfManager.getAll();
		List<AlternateCareFacility> results = new ArrayList<AlternateCareFacility>();
		for(AlternateCareFacilityDTO dto : dtos) {
			AlternateCareFacility acf = new AlternateCareFacility(dto);
			results.add(acf);
		}
       return results;
    }
	
	@ApiOperation(value="Get information about a specific ACF")
	@RequestMapping(value = "/{acfId}", method=RequestMethod.GET)
    public AlternateCareFacility getById(@PathVariable("acfId") Long acfId) {
		AlternateCareFacilityDTO dto = acfManager.getById(acfId);
		return new AlternateCareFacility(dto);
    }
	
	@ApiOperation(value = "Create a new ACF")
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public AlternateCareFacility create(@RequestBody(required=true) AlternateCareFacility toCreate) {
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
		return new AlternateCareFacility(created);
		
	}
	
	@ApiOperation(value = "Edit an existing ACF")
	@RequestMapping(value = "/{acfId}/edit", method = RequestMethod.POST)
	public AlternateCareFacility update(@RequestBody(required=true) AlternateCareFacility toUpdate) 
		throws Exception {
		User user = UserUtil.getCurrentUser();
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
		return new AlternateCareFacility(updated);
		
	}
}
