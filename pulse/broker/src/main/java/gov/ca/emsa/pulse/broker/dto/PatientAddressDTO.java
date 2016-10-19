package gov.ca.emsa.pulse.broker.dto;

import gov.ca.emsa.pulse.broker.entity.PatientAddressEntity;
import gov.ca.emsa.pulse.broker.entity.PatientAddressLineEntity;

import java.util.Date;
import java.util.LinkedList;

public class PatientAddressDTO {
	private Long id;
	private LinkedList<PatientAddressLineDTO> patientAddressLines;
	private String city;
	private String state;
	private String zipcode;
	private Date creationDate;
	private Date lastModifiedDate;
	
	public PatientAddressDTO(){
		this.patientAddressLines = new LinkedList<PatientAddressLineDTO>();
	}
	
	public PatientAddressDTO(PatientAddressEntity entity)
	{
		if(entity != null) {
			this.id = entity.getId();
			if(!entity.getPatientAddressLines().isEmpty()){
				for(PatientAddressLineEntity patientAddressLineEntity : entity.getPatientAddressLines()){
					PatientAddressLineDTO patientAddressLineDto = new PatientAddressLineDTO(patientAddressLineEntity);
					this.patientAddressLines.add(patientAddressLineDto);
				}
			}
			if(entity.getCity() != null)
				this.city = entity.getCity();
			if(entity.getState() != null )
				this.state = entity.getState();
			if(entity.getZipcode() != null)
				this.zipcode = entity.getZipcode();
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

	public LinkedList<PatientAddressLineDTO> getPatientAddressLines() {
		return patientAddressLines;
	}

	public void setPatientAddressLines(
			LinkedList<PatientAddressLineDTO> patientAddressLines) {
		this.patientAddressLines = patientAddressLines;
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
}
