package gov.ca.emsa.pulse.broker.dto;

import gov.ca.emsa.pulse.broker.entity.PatientAddressLineEntity;

public class PatientAddressLineDTO {
	
	private Long id;
	private Long patientAddressId;
	private String line;
	private int lineOrder;
	
	public PatientAddressLineDTO(){}
	
	public PatientAddressLineDTO(PatientAddressLineEntity entity){
		this.id = entity.getId();
		this.patientAddressId = entity.getPatientAddressId();
		this.line = entity.getLine();
		this.lineOrder = entity.getLineOrder();
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getPatientAddressId() {
		return patientAddressId;
	}
	public void setPatientAddressId(Long patientAddressId) {
		this.patientAddressId = patientAddressId;
	}
	public String getLine() {
		return line;
	}
	public void setLine(String line) {
		this.line = line;
	}
	public int getLineOrder() {
		return lineOrder;
	}
	public void setLineOrder(int lineOrder) {
		this.lineOrder = lineOrder;
	}
}
