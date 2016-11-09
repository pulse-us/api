package gov.ca.emsa.pulse.broker.dto;

import gov.ca.emsa.pulse.broker.entity.PatientRecordAddressEntity;
import gov.ca.emsa.pulse.broker.entity.PatientRecordAddressLineEntity;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class PatientRecordAddressDTO {
	private Long id;
	private List<PatientRecordAddressLineDTO> patientRecordAddressLines;
	private Long patientRecordId;
	private String city;
	private String state;
	private String zipcode;
	private Date creationDate;
	private Date lastModifiedDate;
	
	public PatientRecordAddressDTO(){
		this.patientRecordAddressLines = new LinkedList<PatientRecordAddressLineDTO>();
	}
	
	public PatientRecordAddressDTO(PatientRecordAddressEntity entity){
		this();
		if(entity != null) {
			this.id = entity.getId();
			if(entity.getPatientRecordAddressLines() != null){
				for(PatientRecordAddressLineEntity patientAddressLineEntity : entity.getPatientRecordAddressLines()){
					PatientRecordAddressLineDTO patientAddressLineDto = new PatientRecordAddressLineDTO(patientAddressLineEntity);
					this.patientRecordAddressLines.add(patientAddressLineDto);
				}
			}
			if(entity.getCity() != null)
				this.city = entity.getCity();
			if(entity.getState() != null )
				this.state = entity.getState();
			if(entity.getZipcode() != null)
				this.zipcode = entity.getZipcode();
			if(entity.getPatientRecordId() != null)
				this.patientRecordId = entity.getPatientRecordId();
			this.creationDate = entity.getCreationDate();
			this.lastModifiedDate = entity.getLastModifiedDate();
		}
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public List<PatientRecordAddressLineDTO> getPatientRecordAddressLines() {
		return patientRecordAddressLines;
	}

	public void setPatientRecordAddressLines(List<PatientRecordAddressLineDTO> patientRecordAddressLines) {
		this.patientRecordAddressLines = patientRecordAddressLines;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}
	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public Long getPatientRecordId() {
		return patientRecordId;
	}

	public void setPatientRecordId(Long patientRecordId) {
		this.patientRecordId = patientRecordId;
	}
}
