package gov.ca.emsa.pulse.broker.domain;

public enum EndpointTypeEnum {
	PATIENT_DISCOVERY("nwhin-xcpd"),
	DOCUMENT_DISCOVERY("nwhin-xca-query"),
	DOCUMENT_RETRIEVE("nwhin-xca-retrieve");
	
	private String code;
	private EndpointTypeEnum(String code) {
		this.code = code;
	}
	
	public String getCode() {
		return this.code;
	}
}
