package gov.ca.emsa.pulse.broker.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.ColumnTransformer;

@Entity
@Table(name="patient_gender")
public class PatientGenderEntity {
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column( name = "id", nullable = false )
	private Long id;
	
	@Column(name = "code_enc")
	@ColumnTransformer(
			read = "pgp_pub_decrypt(code_enc, dearmor((SELECT * from private_key())))", 
			write = "pgp_pub_encrypt(?, dearmor((SELECT * from public_key())))")
	private String code;
	
	@Column(name = "description_enc")
	@ColumnTransformer(
			read = "pgp_pub_decrypt(description_enc, dearmor((SELECT * from private_key())))", 
			write = "pgp_pub_encrypt(?, dearmor((SELECT * from public_key())))")
	private String description;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
