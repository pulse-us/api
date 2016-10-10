package gov.ca.emsa.pulse.broker.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="given_name")
public class GivenNameEntity {
	
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column( name = "id", nullable = false )
	private Long id;
	
	@Column(name = "name", nullable = false)
	private String givenName;
	
	@Column(name = "patient_name_id", nullable = false)
	private Long patientNameId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getGivenName() {
		return givenName;
	}

	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}

	public Long getPatientNameId() {
		return patientNameId;
	}

	public void setPatientNameId(Long patientNameId) {
		this.patientNameId = patientNameId;
	}
	
}
