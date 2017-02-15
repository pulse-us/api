package gov.ca.emsa.pulse.broker.domain;

public enum EndpointTypeEnum {
	PATIENT_DISCOVERY("ihe-xcpd"),
	DOCUMENT_DISCOVERY("ihe-xca"),
	DOCUMENT_RETRIEVE("ihe-xca");
	
	private String dbName;
	private EndpointTypeEnum(String dbName) {
		this.dbName = dbName;
	}
	
	public String getName() {
		return this.dbName;
	}
}
