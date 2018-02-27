package gov.ca.emsa.pulse.service;

import java.sql.SQLException;

import gov.ca.emsa.pulse.auth.user.CommonUser;
import gov.ca.emsa.pulse.broker.dto.AlternateCareFacilityDTO;
import gov.ca.emsa.pulse.broker.dto.DomainToDtoConverter;
import gov.ca.emsa.pulse.broker.dto.DtoToDomainConverter;
import gov.ca.emsa.pulse.broker.dto.PulseUserDTO;
import gov.ca.emsa.pulse.broker.manager.PulseUserManager;
import gov.ca.emsa.pulse.common.domain.AlternateCareFacility;
import gov.ca.emsa.pulse.common.domain.PulseUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "user")
@RestController
@RequestMapping("/user")
public class PulseUserService {
	
	@Autowired PulseUserManager pulseUserManager;
	
	@ApiOperation(value = "Create a new User")
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	@Secured({"ROLE_ADMIN", "ROLE_ORG_ADMIN"})
	public PulseUser create(@RequestBody(required=true) PulseUser toCreate) {
		
		PulseUserDTO dto = DomainToDtoConverter.convertToPulseUser(toCreate);
		PulseUserDTO created = pulseUserManager.create(dto);
		return DtoToDomainConverter.convertToPulseUser(created);
	}
}
