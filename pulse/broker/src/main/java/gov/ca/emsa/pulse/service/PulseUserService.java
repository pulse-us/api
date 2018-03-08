package gov.ca.emsa.pulse.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import gov.ca.emsa.pulse.broker.dto.DomainToDtoConverter;
import gov.ca.emsa.pulse.broker.dto.DtoToDomainConverter;
import gov.ca.emsa.pulse.broker.dto.PulseUserDTO;
import gov.ca.emsa.pulse.broker.manager.PulseUserManager;
import gov.ca.emsa.pulse.common.domain.PulseUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "user")
@RestController
@RequestMapping("/user")
public class PulseUserService {

    @Autowired
    PulseUserManager pulseUserManager;

    @ApiOperation(value = "Create a new User")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @Secured({
            "ROLE_ADMIN", "ROLE_ORG_ADMIN", "ROLE_PROVIDER"
    })
    public PulseUser create(@RequestBody(required = true) PulseUser toCreate) {

        PulseUserDTO dto = DomainToDtoConverter.convertToPulseUser(toCreate);
        PulseUserDTO created = pulseUserManager.create(dto);
        return DtoToDomainConverter.convertToPulseUser(created);
    }
}
