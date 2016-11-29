package gov.ca.emsa.pulse.broker.dto;

import gov.ca.emsa.pulse.broker.entity.AuditPatientEntity;

public class AuditPatientDTO {
	
	private Long id;
	
	private int participantObjectTypeCode;
	
	private int participantObjectTypeCodeRole;
	
	private String participantObjectDataLifecycle;
	
	private String participantObjectIdTypeCode;
	
	private String participantObjectSensitivity;
	
	private String participantObjectId;
	
	private String participantObjectName;
	
	private String participantObjectQuery;
	
	private String participantObjectDetail;
	
	public AuditPatientDTO(){
		
	}
	
	public AuditPatientDTO(AuditPatientEntity entity){
		this.participantObjectTypeCode = entity.getParticipantObjectTypeCode();
		this.participantObjectTypeCodeRole = entity.getParticipantObjectTypeCodeRole();
		this.participantObjectDataLifecycle = entity.getParticipantObjectDataLifecycle();
		this.participantObjectIdTypeCode = entity.getParticipantObjectIdTypeCode();
		this.participantObjectSensitivity = entity.getParticipantObjectSensitivity();
		this.participantObjectId = entity.getParticipantObjectId();
		this.participantObjectName = entity.getParticipantObjectName();
		this.participantObjectQuery = entity.getParticipantObjectQuery();
		this.participantObjectDetail = entity.getParticipantObjectDetail();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getParticipantObjectTypeCode() {
		return participantObjectTypeCode;
	}

	public void setParticipantObjectTypeCode(int i) {
		this.participantObjectTypeCode = i;
	}

	public int getParticipantObjectTypeCodeRole() {
		return participantObjectTypeCodeRole;
	}

	public void setParticipantObjectTypeCodeRole(int i) {
		this.participantObjectTypeCodeRole = i;
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
