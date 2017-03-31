package gov.ca.emsa.pulse.broker.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.ColumnTransformer;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.IndexColumn;

@Entity
@Table(name="patient_record_address")
public class PatientRecordAddressEntity {
	
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column( name = "id", nullable = false )
	private Long id;
	
	@Column(name = "patient_record_id")
	private Long patientRecordId;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="patientRecordAddressId")
	@Column(name = "patient_record_address_id", nullable = false)
	private List<PatientRecordAddressLineEntity> patientRecordAddressLines = new ArrayList<PatientRecordAddressLineEntity>();
	
	@Column(name = "city_enc")
	@ColumnTransformer(
			read = "pgp_pub_decrypt(city_enc, dearmor((SELECT * from private_key())::text))", 
			write = "pgp_pub_encrypt(?, dearmor((SELECT * from public_key())::text))")
	private String city;
	
	@Column(name = "state_enc")
	@ColumnTransformer(
			read = "pgp_pub_decrypt(state_enc, dearmor((SELECT * from private_key())::text))", 
			write = "pgp_pub_encrypt(?, dearmor((SELECT * from public_key())::text))")
	private String state;
	
	@Column(name = "zipcode_enc")
	@ColumnTransformer(
			read = "pgp_pub_decrypt(zipcode_enc, dearmor((SELECT * from private_key())::text))", 
			write = "pgp_pub_encrypt(?, dearmor((SELECT * from public_key())::text))")
	private String zipcode;
	
	@Column( name = "creation_date", insertable = false, updatable = false)
	private Date creationDate;
	
	@Column( name = "last_modified_date", insertable = false, updatable = false)
	private Date lastModifiedDate;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long long1) {
		this.id = long1;
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

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
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

	public List<PatientRecordAddressLineEntity> getPatientRecordAddressLines() {
		return patientRecordAddressLines;
	}

	public void setPatientRecordAddressLines(
			List<PatientRecordAddressLineEntity> patientRecordAddressLines) {
		this.patientRecordAddressLines = patientRecordAddressLines;
	}

	public Long getPatientRecordId() {
		return patientRecordId;
	}

	public void setPatientRecordId(Long patientRecordId) {
		this.patientRecordId = patientRecordId;
	}

}