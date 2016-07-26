package gov.ca.emsa.pulse.auth.user;

import gov.ca.emsa.pulse.auth.permission.GrantedPermission;
import gov.ca.emsa.pulse.common.domain.AlternateCareFacility;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class CommonUser implements User{

	private static final long serialVersionUID = -4255948572251487878L;
	
	private Long id;
	private String subjectName;
	private String firstName;
	private String lastName;
    private String email;
	private Set<GrantedPermission> permissions = new HashSet<GrantedPermission>();
	private boolean authenticated = true;
    private String jwt;
    private AlternateCareFacility acf;
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getSubjectName() {
		return subjectName;
	}
	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Set<GrantedPermission> getPermissions() {
		return permissions;
	}
	public void setPermissions(Set<GrantedPermission> permissions) {
		this.permissions = permissions;
	}
	public boolean isAuthenticated() {
		return authenticated;
	}
	public void setAuthenticated(boolean authenticated) {
		this.authenticated = authenticated;
	}
	public String getJwt() {
		return jwt;
	}
	public void setJwt(String jwt) {
		this.jwt = jwt;
	}
	public AlternateCareFacility getAcf() {
		return acf;
	}
	public void setAcf(AlternateCareFacility acf) {
		this.acf = acf;
	}
	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public Object getCredentials() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void addPermission(GrantedPermission permission) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void removePermission(String permissionValue) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Collection<GrantedPermission> getAuthorities() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Object getDetails() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Object getPrincipal() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

}
