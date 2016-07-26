package gov.ca.emsa.pulse.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import gov.ca.emsa.pulse.auth.user.CommonUser;
import gov.ca.emsa.pulse.auth.user.User;

public class UserUtil {

	public static CommonUser getCurrentUser(){
		CommonUser user = new CommonUser();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (auth instanceof User){
			user.setAcf(((User) auth).getAcf());
			user.setId(((User) auth).getId());
			user.setFirstName(((User) auth).getFirstName());
			user.setLastName(((User) auth).getLastName());
			user.setEmail(((User) auth).getEmail());
			user.setSubjectName(((User)auth).getSubjectName());
		}
		return user;
	}
}
