package gov.ca.emsa.pulse.broker.domain;

public class User {
	private String userKey;
	private String name;
	private Long acfId; 
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getAcfId() {
		return acfId;
	}

	public void setAcfId(Long acfId) {
		this.acfId = acfId;
	}

	public String getUserKey() {
		return userKey;
	}

	public void setUserKey(String userKey) {
		this.userKey = userKey;
	}

}
