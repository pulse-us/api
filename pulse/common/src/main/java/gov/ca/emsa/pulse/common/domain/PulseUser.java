package gov.ca.emsa.pulse.common.domain;

public class PulseUser {
	
	private Long id;
	private String assertion;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getAssertion() {
		return assertion;
	}
	public void setAssertion(String assertion) {
		this.assertion = assertion;
	}
}
