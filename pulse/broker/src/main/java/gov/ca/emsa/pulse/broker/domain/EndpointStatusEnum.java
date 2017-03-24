package gov.ca.emsa.pulse.broker.domain;

public enum EndpointStatusEnum {
	ACTIVE("Active"),
	SUSPENDED("Suspended"),
	ERROR("Error"), 
	OFF("Off"),
	ENTEREDINERROR("Entered-in-error"),
	TEST("Test");
	
	private String dbName;
	private EndpointStatusEnum(String dbName) {
		this.dbName = dbName;
	}
	
	public String getName() {
		return this.dbName;
	}
}
