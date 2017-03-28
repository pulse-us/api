package gov.ca.emsa.pulse.service;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.security.Principal;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "authenticate")
@RestController
@RequestMapping("/authenticate")
public class AuthenticateController {
	@ApiOperation(value="Authenticate",
                  notes="")
    @RequestMapping("/authenticate")
    public Principal user (Principal user) {
        return user;
    }
}
