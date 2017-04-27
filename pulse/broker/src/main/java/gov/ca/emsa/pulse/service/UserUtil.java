package gov.ca.emsa.pulse.service;

import gov.ca.emsa.pulse.auth.user.CommonUser;
import gov.ca.emsa.pulse.auth.user.JWTAuthenticatedUser;
import gov.ca.emsa.pulse.auth.user.User;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class UserUtil {

	public static CommonUser getCurrentUser(){
		CommonUser user = new CommonUser();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if(auth instanceof User) {
			User jwtAuth = (User) auth;
			user.setuser_id(jwtAuth.getuser_id());
			user.setSubjectName(jwtAuth.getSubjectName());
			user.setusername(jwtAuth.getUsername());
			user.setEmail(jwtAuth.getEmail());
			user.setauth_source(jwtAuth.getauth_source());
			user.setfull_name(jwtAuth.getfull_name());
			user.setorganization(jwtAuth.getorganization());
			user.setpurpose_for_use(jwtAuth.getpurpose_for_use());
			user.setrole(jwtAuth.getrole());
			user.setFirstName(jwtAuth.getFirstName());
			user.setLastName(jwtAuth.getLastName());
			user.setPulseUserId(jwtAuth.getPulseUserId());
			user.setAcf(jwtAuth.getAcf());
		} 
		return user;
	}
}
