package gov.ca.emsa.pulse.broker.dto;

import gov.ca.emsa.pulse.broker.entity.PatientRecordAddressLineEntity;

public class PatientRecordAddressLineDTO {
	
	private Long id;
	private Long patientRecordAddressId;
	private String line;
	private int lineOrder;
	
	public PatientRecordAddressLineDTO(){}
	
	public PatientRecordAddressLineDTO(PatientRecordAddressLineEntity entity){
		this.id = entity.getId();
		this.patientRecordAddressId = entity.getPatientRecordAddressId();
		this.line = entity.getLine();
		this.lineOrder = entity.getLineOrder();
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getPatientRecordAddressId() {
		return patientRecordAddressId;
	}
	public void setPatientRecordAddressId(Long patientRecordAddressId) {
		this.patientRecordAddressId = patientRecordAddressId;
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
