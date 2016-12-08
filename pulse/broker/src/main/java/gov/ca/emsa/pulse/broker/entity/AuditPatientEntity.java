package gov.ca.emsa.pulse.broker.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="audit_patient")
public class AuditPatientEntity {
	
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column( name = "id", nullable = false )
	private Long id;
	
	@Column(name="participant_object_type_code") // "1" (person)
	private int participantObjectTypeCode;
	
	@Column(name="participant_object_type_code_role") // "1" (patient)
	private int participantObjectTypeCodeRole;
	
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getParticipantObjectTypeCode() {
		return participantObjectTypeCode;
	}

	public void setParticipantObjectTypeCode(int participantObjectTypeCode) {
		this.participantObjectTypeCode = participantObjectTypeCode;
	}

	public int getParticipantObjectTypeCodeRole() {
		return participantObjectTypeCodeRole;
	}

	public void setParticipantObjectTypeCodeRole(int participantObjectTypeCodeRole) {
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

}
