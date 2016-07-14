package gov.ca.emsa.pulse.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import gov.ca.emsa.pulse.auth.user.JWTAuthenticatedUser;

public class UserUtil {

	public static JWTAuthenticatedUser getCurrentUser(){
		JWTAuthenticatedUser user = null;

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (auth instanceof JWTAuthenticatedUser){
			user = (JWTAuthenticatedUser) auth;
		}
		return user;
	}
}
