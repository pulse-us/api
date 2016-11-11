package gov.ca.emsa.pulse.common.domain;

public class PatientRecordAddressLine implements Comparable<PatientRecordAddressLine>{
	
	private Long id;
	private Long patientRecordAddressId;
	private String line;
	private int lineOrder;
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
	
	public int compareTo(PatientRecordAddressLine o) {
		if(this.lineOrder == o.lineOrder){
			return 0;
		}else if(this.lineOrder < o.lineOrder){
			return -1;
		}else{
			return 1;
		}
	}

}
