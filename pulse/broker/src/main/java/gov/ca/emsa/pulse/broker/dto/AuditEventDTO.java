package gov.ca.emsa.pulse.broker.dto;

import gov.ca.emsa.pulse.broker.entity.AuditDocumentEntity;
import gov.ca.emsa.pulse.broker.entity.AuditEventEntity;
import gov.ca.emsa.pulse.broker.entity.AuditHumanRequestorEntity;

import java.util.ArrayList;
import java.util.Date;

public class AuditEventDTO {
	
	private Long id;
	
	private String eventId;
	
	private Long eventActionCodeId;
	
	private EventActionCodeDTO eventActionCode;
	
	private String eventDateTime;
	
	private String eventOutcomeIndicator;
	
	private String eventTypeCode;
	
	private Long auditRequestSourceId;
	
	private AuditRequestSourceDTO auditRequestSource;
	
	private Long auditRequestDestinationId;
	
	private AuditRequestDestinationDTO auditRequestDestination;
	
	private Long auditSourceId;
	
	private AuditSourceDTO auditSource;
	
	private Long auditQueryParametersId;
	
	private AuditQueryParametersDTO auditQueryParameters;
	
	private Long auditPatientId;
	
	private AuditPatientDTO auditPatient;
	
	private ArrayList<AuditHumanRequestorDTO> auditHumanRequestors;
	
	private ArrayList<AuditDocumentDTO> auditDocument;
	
	private Date creationDate;
	
	private Date lastModifiedDate;
	
	public AuditEventDTO() {
		auditHumanRequestors = new ArrayList<AuditHumanRequestorDTO>();
		auditDocument = new ArrayList<AuditDocumentDTO>();
	}
	
	public AuditEventDTO(AuditEventEntity entity) {
		this();
		this.id = entity.getId();
		this.eventId = entity.getEventId();
		this.eventActionCodeId = entity.getEventActionCodeId();
		//this.eventActionCode = new EventActionCodeDTO(entity.getEventActionCode());
		this.eventDateTime = entity.getEventDateTime();
		this.eventOutcomeIndicator = entity.getEventOutcomeIndicator();
		this.eventTypeCode = entity.getEventTypeCode();
		this.auditPatientId = entity.getAuditPatientId();
		this.auditQueryParametersId = entity.getAuditQueryParametersId();
		this.auditRequestDestinationId = entity.getAuditRequestDestinationId();
		this.auditRequestSourceId = entity.getAuditRequestSourceId();
		this.auditSourceId = entity.getAuditSourceId();
		this.auditRequestSource = new AuditRequestSourceDTO(entity.getAuditRequestSource());
		this.auditRequestDestination = new AuditRequestDestinationDTO(entity.getAuditRequestDestination());
		this.auditSource = new AuditSourceDTO(entity.getAuditSource());
		if(entity.getAuditQueryParameters() != null){
			this.auditQueryParameters = new AuditQueryParametersDTO(entity.getAuditQueryParameters());
		}
		if(entity.getAuditPatient() != null){
			this.auditPatient = new AuditPatientDTO(entity.getAuditPatient());
		}
		if(entity.getAuditDocument() != null){
			for(AuditDocumentEntity auditDocumentEntity : entity.getAuditDocument()){
				this.auditDocument.add(new AuditDocumentDTO(auditDocumentEntity));
			}
		}
		if(entity.getAuditHumanRequestor() != null){
			for(AuditHumanRequestorEntity humanRequestorEntity : entity.getAuditHumanRequestor()){
				this.auditHumanRequestors.add(new AuditHumanRequestorDTO(humanRequestorEntity));
			}
		}
		this.creationDate = entity.getCreationDate();
		this.lastModifiedDate = entity.getLastModifiedDate();
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

	public EventActionCodeDTO getEventActionCode() {
		return eventActionCode;
	}

	public void setEventActionCode(EventActionCodeDTO eventActionCode) {
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

	public ArrayList<AuditDocumentDTO> getAuditDocument() {
		return auditDocument;
	}

	public void setAuditDocument(ArrayList<AuditDocumentDTO> auditDocument) {
		this.auditDocument = auditDocument;
	}

	public Long getAuditRequestSourceId() {
		return auditRequestSourceId;
	}

	public void setAuditRequestSourceId(Long auditRequestSourceId) {
		this.auditRequestSourceId = auditRequestSourceId;
	}

	public Long getAuditRequestDestinationId() {
		return auditRequestDestinationId;
	}

	public void setAuditRequestDestinationId(Long auditRequestDestinationId) {
		this.auditRequestDestinationId = auditRequestDestinationId;
	}

	public Long getAuditSourceId() {
		return auditSourceId;
	}

	public void setAuditSourceId(Long auditSourceId) {
		this.auditSourceId = auditSourceId;
	}

	public Long getAuditQueryParametersId() {
		return auditQueryParametersId;
	}

	public void setAuditQueryParametersId(Long auditQueryParametersId) {
		this.auditQueryParametersId = auditQueryParametersId;
	}

	public Long getAuditPatientId() {
		return auditPatientId;
	}

	public void setAuditPatientId(Long auditPatientId) {
		this.auditPatientId = auditPatientId;
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

	public Long getEventActionCodeId() {
		return eventActionCodeId;
	}

	public void setEventActionCodeId(Long eventActionCodeId) {
		this.eventActionCodeId = eventActionCodeId;
	}
	
	
	
}
