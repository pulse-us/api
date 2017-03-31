package gov.ca.emsa.pulse.broker.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.ColumnTransformer;

@Entity
@Table(name="given_name")
public class GivenNameEntity {
	
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column( name = "id", nullable = false )
	private Long id;
	
	@Column(name = "name_enc", nullable = false)
	@ColumnTransformer(
			read = "pgp_pub_decrypt(name_enc, dearmor((SELECT * from private_key())::text))", 
			write = "pgp_pub_encrypt(?, dearmor((SELECT * from public_key())::text))")
	private String givenName;
	
	@Column(name = "patient_record_name_id")
	private Long patientRecordNameId;
	
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

	public Long getPatientRecordNameId() {
		return patientRecordNameId;
	}

	public void setPatientRecordNameId(Long patientRecordNameId) {
		this.patientRecordNameId = patientRecordNameId;
	}
	
}
