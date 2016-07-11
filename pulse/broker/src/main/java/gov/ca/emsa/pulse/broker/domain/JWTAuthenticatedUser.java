package gov.ca.emsa.pulse.broker.domain;

import gov.ca.emsa.pulse.common.domain.User;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

public class JWTAuthenticatedUser implements User {

	private static final long serialVersionUID = 1L;
	private Long id;
	private String subjectName;
	private String firstName;
	private String lastName;
    private String email;
	private boolean authenticated = true;
	private String acf;
	
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

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return null;
	}

	@Override
	public Object getDetails() {
		return this;
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

	public String getAcf() {
		return acf;
	}

	public void setAcf(String acf) {
		this.acf = acf;
	}

}
