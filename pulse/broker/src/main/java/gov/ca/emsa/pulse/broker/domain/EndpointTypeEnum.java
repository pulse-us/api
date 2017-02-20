package gov.ca.emsa.pulse.broker.domain;

public enum EndpointTypeEnum {
	PATIENT_DISCOVERY("ihe-xcpd"),
	DOCUMENT_DISCOVERY("ihe-xca"),
	DOCUMENT_RETRIEVE("ihe-xca");
	
	private String code;
	private EndpointTypeEnum(String code) {
		this.code = code;
	}
	
	public String getCode() {
		return this.code;
	}
}
