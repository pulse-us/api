package gov.ca.emsa.pulse.auth.user;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import gov.ca.emsa.pulse.auth.permission.GrantedPermission;
import gov.ca.emsa.pulse.common.domain.AlternateCareFacility;
@JsonIgnoreProperties(ignoreUnknown = true)
public class JWTAuthenticatedUser implements User {

	private static final long serialVersionUID = 1L;
	private Long id;
	private String subjectName;
	private String firstName;
	private String lastName;
    private String email;
	private Set<GrantedPermission> permissions = new HashSet<GrantedPermission>();
	private AlternateCareFacility acf;
    private HashMap<String,String> details = new HashMap<String,String>();
	private boolean authenticated = true;

	public JWTAuthenticatedUser(){
		this.subjectName = null;
	}

	public JWTAuthenticatedUser(String subjectName) {
		this.subjectName = subjectName;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subject) {
		this.subjectName = subject;
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
		return this.permissions;
	}

	public void addPermission(GrantedPermission permission){
		this.permissions.add(permission);
	}

	public void addPermission(String permissionValue) {
		GrantedPermission permission = new GrantedPermission(permissionValue);
		this.permissions.add(permission);
	}

	@Override
	public void removePermission(String permissionValue){
		this.permissions.remove(new GrantedPermission(permissionValue));
	}

	@Override
	public Collection<GrantedPermission> getAuthorities() {
		return this.getPermissions();
	}

	@Override
	public Object getDetails() {
		return details;
	}

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
	public Object getPrincipal() {
		return this.getName();
	}

    @Override
	public Object getCredentials() {
		return this.getName();
	}

    @Override
	public String getPassword() {
		return "";
	}

	@Override
	public boolean isAuthenticated() {
		return this.authenticated;
	}

	@Override
	public void setAuthenticated(boolean arg0) throws IllegalArgumentException {
		this.authenticated = arg0;
	}

    public String getJwt() {
        return details.get("jwt");
    }

    public void setJwt(String jwt) {
        details.put("jwt",jwt);
    }

	@Override
	public String getName() {
		return subjectName;
	}

	@Override
	public String getUsername() {
		return subjectName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public AlternateCareFacility getAcf() {
		return acf;
	}

	public void setAcf(AlternateCareFacility acf) {
		this.acf = acf;
	}
	
    @Override
    public String toString() {
        String ret = "{User: " +
            "[subjectName: " + subjectName + "]" +
            "[firstName: " + firstName + "]" +
            "[lastName: " + lastName + "]" +
            "[email: " + email + "]" +
            "[jwt: " + details.get("jwt") + "]" +
            "[acf: " + details.get("acf") + "]}";
        return ret;
    }
}
