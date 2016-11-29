package gov.ca.emsa.pulse.broker.dto;

import gov.ca.emsa.pulse.broker.entity.AuditEventEntity;
import gov.ca.emsa.pulse.broker.entity.AuditHumanRequestorEntity;

import java.util.ArrayList;

public class AuditEventDTO {
	
	private Long id;
	
	private String eventId;
	
	private String eventActionCode;
	
	private String eventDateTime;
	
	private String eventOutcomeIndicator;
	
	private String eventTypeCode;
	
	private AuditRequestSourceDTO auditRequestSource;
	
	private AuditRequestDestinationDTO auditRequestDestination;
	
	private AuditSourceDTO auditSource;
	
	private AuditQueryParametersDTO auditQueryParameters;
	
	private ArrayList<AuditHumanRequestorDTO> auditHumanRequestors;
	
	private AuditPatientDTO auditPatient;
	
	public AuditEventDTO() {
		auditHumanRequestors = new ArrayList<AuditHumanRequestorDTO>();
	}
	
	public AuditEventDTO(AuditEventEntity entity) {
		this();
		this.id = entity.getId();
		this.eventId = entity.getEventId();
		this.eventActionCode = entity.getEventActionCode();
		this.eventDateTime = entity.getEventDateTime();
		this.eventOutcomeIndicator = entity.getEventOutcomeIndicator();
		this.eventTypeCode = entity.getEventTypeCode();
		this.auditRequestSource = new AuditRequestSourceDTO(entity.getAuditRequestSource());
		this.auditRequestDestination = new AuditRequestDestinationDTO(entity.getAuditRequestDestination());
		this.auditSource = new AuditSourceDTO(entity.getAuditSource());
		this.auditQueryParameters = new AuditQueryParametersDTO(entity.getAuditQueryParameters());
		if(entity.getAuditHumanRequestor() != null){
			for(AuditHumanRequestorEntity humanRequestorEntity : entity.getAuditHumanRequestor()){
				this.auditHumanRequestors.add(new AuditHumanRequestorDTO(humanRequestorEntity));
			}
		}
		if(entity.getAuditPatient() != null){
			this.auditPatient = new AuditPatientDTO(entity.getAuditPatient());
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public String getEventActionCode() {
		return eventActionCode;
	}

	public void setEventActionCode(String eventActionCode) {
		this.eventActionCode = eventActionCode;
	}

	public String getEventDateTime() {
		return eventDateTime;
	}

	public void setEventDateTime(String eventDateTime) {
		this.eventDateTime = eventDateTime;
	}

	public String getEventOutcomeIndicator() {
		return eventOutcomeIndicator;
	}

	public void setEventOutcomeIndicator(String eventOutcomeIndicator) {
		this.eventOutcomeIndicator = eventOutcomeIndicator;
	}

	public String getEventTypeCode() {
		return eventTypeCode;
	}

	public void setEventTypeCode(String eventTypeCode) {
		this.eventTypeCode = eventTypeCode;
	}

	public AuditRequestSourceDTO getAuditRequestSource() {
		return auditRequestSource;
	}

	public void setAuditRequestSource(AuditRequestSourceDTO auditRequestSource) {
		this.auditRequestSource = auditRequestSource;
	}

	public AuditRequestDestinationDTO getAuditRequestDestination() {
		return auditRequestDestination;
	}

	public void setAuditRequestDestination(
			AuditRequestDestinationDTO auditRequestDestination) {
		this.auditRequestDestination = auditRequestDestination;
	}

	public AuditSourceDTO getAuditSource() {
		return auditSource;
	}

	public void setAuditSource(AuditSourceDTO auditSource) {
		this.auditSource = auditSource;
	}

	public AuditQueryParametersDTO getAuditQueryParameters() {
		return auditQueryParameters;
	}

	public void setAuditQueryParameters(AuditQueryParametersDTO auditQueryParameters) {
		this.auditQueryParameters = auditQueryParameters;
	}

	public ArrayList<AuditHumanRequestorDTO> getAuditHumanRequestors() {
		return auditHumanRequestors;
	}

	public void setAuditHumanRequestors(
			ArrayList<AuditHumanRequestorDTO> auditHumanRequestors) {
		this.auditHumanRequestors = auditHumanRequestors;
	}

	public AuditPatientDTO getAuditPatient() {
		return auditPatient;
	}

	public void setAuditPatient(AuditPatientDTO auditPatient) {
		this.auditPatient = auditPatient;
	}
	
}
