package gov.ca.emsa.pulse.broker.entity;

import java.util.Date;

import gov.ca.emsa.pulse.broker.dto.ParticipantObjectTypeCodeDTO;
import gov.ca.emsa.pulse.broker.dto.ParticipantObjectTypeCodeRoleDTO;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="audit_patient")
public class AuditPatientEntity {
	
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column( name = "id", nullable = false )
	private Long id;
	
	@Column(name="participant_object_type_code_id")
	private Long participantObjectTypeCodeId;
	
	@OneToOne
	@JoinColumn(name="participant_object_type_code_id", insertable = false, updatable = false)
	private ParticipantObjectTypeCodeEntity participantObjectTypeCode;
	
	@Column(name="participant_object_type_code_role_id")
	private Long participantObjectTypeCodeRoleId;
	
	@OneToOne
	@JoinColumn(name="participant_object_type_code_role_id", insertable = false, updatable = false)
	private ParticipantObjectTypeCodeRoleEntity participantObjectTypeCodeRole;
	
	@Column(name="participant_object_data_lifecycle")
	private String participantObjectDataLifecycle;
	
	@Column(name="participant_object_id_type_code")
	private String participantObjectIdTypeCode;
	
	@Column(name="participant_object_sensitivity")
	private String participantObjectSensitivity;
	
	@Column(name="participant_object_id") // The patient ID in HL7 CX format (see ITI TF-2x: appendix E).
	private String participantObjectId;
	
	@Column(name="participant_object_name")
	private String participantObjectName;
	
	@Column(name="participant_object_query")
	private String participantObjectQuery;
	
	@Column(name="participant_object_detail")
	private String participantObjectDetail;
	
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

	public Long getParticipantObjectTypeCodeId() {
		return participantObjectTypeCodeId;
	}

	public void setParticipantObjectTypeCodeId(Long participantObjectTypeCodeId) {
		this.participantObjectTypeCodeId = participantObjectTypeCodeId;
	}

	public ParticipantObjectTypeCodeEntity getParticipantObjectTypeCode() {
		return participantObjectTypeCode;
	}

	public void setParticipantObjectTypeCode(
			ParticipantObjectTypeCodeEntity participantObjectTypeCode) {
		this.participantObjectTypeCode = participantObjectTypeCode;
	}

	public Long getParticipantObjectTypeCodeRoleId() {
		return participantObjectTypeCodeRoleId;
	}

	public void setParticipantObjectTypeCodeRoleId(
			Long participantObjectTypeCodeRoleId) {
		this.participantObjectTypeCodeRoleId = participantObjectTypeCodeRoleId;
	}

	public ParticipantObjectTypeCodeRoleEntity getParticipantObjectTypeCodeRole() {
		return participantObjectTypeCodeRole;
	}

	public void setParticipantObjectTypeCodeRole(
			ParticipantObjectTypeCodeRoleEntity participantObjectTypeCodeRole) {
		this.participantObjectTypeCodeRole = participantObjectTypeCodeRole;
	}

	public String getParticipantObjectDataLifecycle() {
		return participantObjectDataLifecycle;
	}

	public void setParticipantObjectDataLifecycle(
			String participantObjectDataLifecycle) {
		this.participantObjectDataLifecycle = participantObjectDataLifecycle;
	}

	public String getParticipantObjectIdTypeCode() {
		return participantObjectIdTypeCode;
	}

	public void setParticipantObjectIdTypeCode(String participantObjectIdTypeCode) {
		this.participantObjectIdTypeCode = participantObjectIdTypeCode;
	}

	public String getParticipantObjectSensitivity() {
		return participantObjectSensitivity;
	}

	public void setParticipantObjectSensitivity(String participantObjectSensitivity) {
		this.participantObjectSensitivity = participantObjectSensitivity;
	}

	public String getParticipantObjectId() {
		return participantObjectId;
	}

	public void setParticipantObjectId(String participantObjectId) {
		this.participantObjectId = participantObjectId;
	}

	public String getParticipantObjectName() {
		return participantObjectName;
	}

	public void setParticipantObjectName(String participantObjectName) {
		this.participantObjectName = participantObjectName;
	}

	public String getParticipantObjectQuery() {
		return participantObjectQuery;
	}

	public void setParticipantObjectQuery(String participantObjectQuery) {
		this.participantObjectQuery = participantObjectQuery;
	}

	public String getParticipantObjectDetail() {
		return participantObjectDetail;
	}

	public void setParticipantObjectDetail(String participantObjectDetail) {
		this.participantObjectDetail = participantObjectDetail;
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
	
	

}
