package gov.ca.emsa.pulse.broker.domain;

public enum EndpointTypeEnum {
	PATIENT_DISCOVERY("Patient Discovery"),
	DOCUMENT_DISCOVERY("Query for Documents"),
	DOCUMENT_RETRIEVE("Retrieve Documents");
	
	private String dbName;
	private EndpointTypeEnum(String dbName) {
		this.dbName = dbName;
	}
	
	public String getName() {
		return this.dbName;
	}
}
