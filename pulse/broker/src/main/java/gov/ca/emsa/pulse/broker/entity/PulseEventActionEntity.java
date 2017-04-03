package gov.ca.emsa.pulse.broker.entity;

import java.util.Date;

import gov.ca.emsa.pulse.broker.dto.NetworkAccessPointTypeCodeDTO;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.ColumnTransformer;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Table(name="pulse_event_action")
public class PulseEventActionEntity {
	
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column( name = "id", nullable = false)
	private Long id;
	
	@Column(name="username")
	private String username;
	
	@Column(name="action_tstamp", insertable = false, updatable = false)
	private Date actionTStamp;
	
	@Column(name="action_json_enc")
	@ColumnTransformer(
			read = "pgp_pub_decrypt(action_json_enc, dearmor((SELECT * from private_key())))", 
			write = "pgp_pub_encrypt(?, dearmor((SELECT * from public_key())))")
	private String actionJson;
	
	@Column(name="pulse_event_action_code_id")
	private Long pulseEventActionCodeId;
	
	@OneToOne(optional = true, fetch = FetchType.LAZY)
	@Fetch(FetchMode.JOIN)
	@JoinColumn(name = "pulse_event_action_code_id", unique=true, nullable = true, insertable=false, updatable= false)
	private PulseEventActionCodeEntity code;
	
	@Column( name = "creation_date", insertable = false, updatable = false)
	private Date creationDate;
	
	@Column( name = "last_modified_date", insertable = false, updatable = false)
	private Date lastModifiedDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Date getActionTStamp() {
		return actionTStamp;
	}

	public void setActionTStamp(Date actionTStamp) {
		this.actionTStamp = actionTStamp;
	}

	public String getActionJson() {
		return actionJson;
	}

	public void setActionJson(String actionJson) {
		this.actionJson = actionJson;
	}

	public Long getPulseEventActionCodeId() {
		return pulseEventActionCodeId;
	}

	public void setPulseEventActionCodeId(Long pulseEventActionCodeId) {
		this.pulseEventActionCodeId = pulseEventActionCodeId;
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

	public PulseEventActionCodeEntity getCode() {
		return code;
	}

	public void setCode(PulseEventActionCodeEntity code) {
		this.code = code;
	}
	
	

}
