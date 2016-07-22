package gov.ca.emsa.pulse.auth.user;

import gov.ca.emsa.pulse.auth.permission.GrantedPermission;
import gov.ca.emsa.pulse.common.domain.AlternateCareFacility;

import java.util.Collection;
import java.util.Set;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

public interface User extends UserDetails , Authentication {

	public Long getId();
	public String getSubjectName();
	public void setSubjectName(String subject);

	public void setFirstName(String firstName);
	public String getFirstName();
	public void setLastName(String lastName);
	public String getLastName();
    public void setEmail(String email);
    public String getEmail();
    public void setAcf(AlternateCareFacility acf);
    public AlternateCareFacility getAcf();

	public Set<GrantedPermission> getPermissions();
	public void addPermission(GrantedPermission permission);
	public void removePermission(String permissionValue);


	// UserDetails interface
	@Override
	public String getUsername();

	// Authentication Interface
	@Override
	public Collection<GrantedPermission> getAuthorities();

	@Override
	public Object getPrincipal();

	@Override
	public boolean isAuthenticated();

	@Override
	public void setAuthenticated(boolean arg0) throws IllegalArgumentException;

	@Override
	public String getName();
}
