package gov.ca.emsa.pulse.auth.user;

import gov.ca.emsa.pulse.auth.permission.GrantedPermission;
import gov.ca.emsa.pulse.common.domain.AlternateCareFacility;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.opensaml.saml2.core.Assertion;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@JsonIgnoreProperties(ignoreUnknown = true)
public class JWTAuthenticatedUser implements User {

	private static final long serialVersionUID = 1L;
	private Long id;
	private String subjectName;

    // Provided by SAML assertion
    private String user_id;
    private String username;
    private String auth_source;
    private String full_name;
    private String organization;
    private String purpose_for_use;
    private String role;
    private String assertion;

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

    public String getuser_id() {
        return user_id;
    }

    public void setuser_id(String user_id) {
        this.user_id = user_id;
    }

    @Override
    public String getUsername () {
        return username;
    }

    public void setusername(String username) {
        this.username = username;
    }

    public String getauth_source () {
        return auth_source;
    }

    public void setauth_source(String auth_source) {
        this.auth_source = auth_source;
    }

    public String getfull_name () {
        return full_name;
    }

    public void setfull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getorganization () {
        return organization;
    }

    public void setorganization(String organization) {
        this.organization = organization;
    }

    public String getpurpose_for_use () {
        return purpose_for_use;
    }

    public void setpurpose_for_use(String purpose_for_use) {
        this.purpose_for_use = purpose_for_use;
    }

    public String getrole () {
        return role;
    }

    public void setrole(String role) {
        this.role = role;
    }

	public String getAssertion() {
		return assertion;
	}

	public void setAssertion(String assertion) {
		this.assertion = assertion;
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

	public void setFirstName(String firstName) {};
	public String getFirstName() { return "N/A"; };
	public void setLastName(String lastName) {};
	public String getLastName() { return "N/A"; };
    public void setEmail(String email) {};
    public String getEmail() { return "N/A"; }

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
            "[user_id: " + user_id + "]" +
            "[username: " + username + "]" +
            "[auth_source: " + auth_source + "]" +
            "[full_name: " + full_name + "]" +
            "[organization: " + organization + "]" +
            "[purpose_for_use: " + purpose_for_use + "]" +
            "[role: " + role + "]" +
            "[assertion: " + assertion.toString() + "]" +
            "[jwt: " + details.get("jwt") + "]";
        return ret;
    }
}
