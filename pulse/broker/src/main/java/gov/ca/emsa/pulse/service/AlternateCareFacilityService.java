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
	@RequestMapping(value="",  method = RequestMethod.POST)
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
	@RequestMapping(value = "/{acfId}", method=RequestMethod.POST)
    public AlternateCareFacility getById(@PathVariable("acfId") Long acfId) {
		AlternateCareFacilityDTO dto = acfManager.getById(acfId);
		return new AlternateCareFacility(dto);
    }
	
	@ApiOperation(value = "Create a new ACF")
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public AlternateCareFacility create(@RequestBody(required=true) AlternateCareFacility toCreate) {
		AlternateCareFacilityDTO dto = new AlternateCareFacilityDTO();
		dto.setName(toCreate.getName());
		if(toCreate.getAddress() != null) {
			AddressDTO address = new AddressDTO();
			address.setStreetLineOne(toCreate.getAddress().getStreetLine1());
			address.setStreetLineTwo(toCreate.getAddress().getStreetLine2());
			address.setCity(toCreate.getAddress().getCity());
			address.setState(toCreate.getAddress().getState());
			address.setZipcode(toCreate.getAddress().getZipcode());
			dto.setAddress(address);
		}
		AlternateCareFacilityDTO created = acfManager.create(dto);
		return new AlternateCareFacility(created);
		
	}
}
