package gov.ca.emsa.pulse.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import gov.ca.emsa.pulse.common.domain.User;


public class UserUtil {

	public static User getCurrentUser(){	
		User user = null;
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		if (auth instanceof User){
			user = (User) auth;
		}
		return user;	
	}
}
