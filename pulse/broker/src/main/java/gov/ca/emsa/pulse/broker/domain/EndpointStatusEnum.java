package gov.ca.emsa.pulse.broker.domain;

public enum EndpointStatusEnum {
	ACTIVE("active"),
	INACTIVE("inactive");
	
	private String dbName;
	private EndpointStatusEnum(String dbName) {
		this.dbName = dbName;
	}
	
	public String getName() {
		return this.dbName;
	}
}
