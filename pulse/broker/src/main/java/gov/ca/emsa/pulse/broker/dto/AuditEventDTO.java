package gov.ca.emsa.pulse.broker.dto;

import gov.ca.emsa.pulse.broker.audit.AuditQueryParameters;
import gov.ca.emsa.pulse.broker.audit.AuditRequestDestination;
import gov.ca.emsa.pulse.broker.audit.AuditRequestSource;
import gov.ca.emsa.pulse.broker.audit.AuditSource;
import gov.ca.emsa.pulse.broker.entity.AuditEventEntity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class AuditEventDTO {
	
	private Long id;
	
	private String eventId;
	
	private String eventActionCode;
	
	private String eventDateTime;
	
	private String eventOutcomeIndicator;
	
	private String eventTypeCode;
	
	private AuditRequestSource auditRequestSourceId;
	
	private AuditRequestDestination auditRequestDestinationId;
	
	private AuditSource auditSourceId;
	
	private AuditQueryParameters auditQueryParametersId;
	
	public AuditEventDTO() {
	}
	
	public AuditEventDTO(AuditEventEntity entity) {
		this.id = entity.getId();
		this.eventId = entity.getEventId();
		this.eventActionCode = entity.getEventActionCode();
		this.eventDateTime = entity.getEventDateTime();
		this.eventOutcomeIndicator = entity.getEventOutcomeIndicator();
		this.eventTypeCode = entity.getEventTypeCode();
		this.auditRequestSourceId = entity.getAuditRequestSourceId();
		this.auditRequestDestinationId = entity.getAuditRequestDestinationId();
		this.auditSourceId = entity.getAuditSourceId();
		this.auditQueryParametersId = entity.getAuditQueryParametersId();
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

	public Date getEventOutcomeIndicator() {
		return eventOutcomeIndicator;
	}

	public void setEventOutcomeIndicator(Date eventOutcomeIndicator) {
		this.eventOutcomeIndicator = eventOutcomeIndicator;
	}

	public Date getEventTypeCode() {
		return eventTypeCode;
	}

	public void setEventTypeCode(Date eventTypeCode) {
		this.eventTypeCode = eventTypeCode;
	}

	public Date getAuditRequestSourceId() {
		return auditRequestSourceId;
	}

	public void setAuditRequestSourceId(Date auditRequestSourceId) {
		this.auditRequestSourceId = auditRequestSourceId;
	}

	public Date getAuditRequestDestinationId() {
		return auditRequestDestinationId;
	}

	public void setAuditRequestDestinationId(Date auditRequestDestinationId) {
		this.auditRequestDestinationId = auditRequestDestinationId;
	}

	public Date getAuditSourceId() {
		return auditSourceId;
	}

	public void setAuditSourceId(Date auditSourceId) {
		this.auditSourceId = auditSourceId;
	}

	public Date getAuditQueryParametersId() {
		return auditQueryParametersId;
	}

	public void setAuditQueryParametersId(Date auditQueryParametersId) {
		this.auditQueryParametersId = auditQueryParametersId;
	}
	
}
