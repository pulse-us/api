package gov.ca.emsa.pulse.broker.audit;

import java.util.List;

public class AuditEvent {
	
	private Long id;
	
	private String eventId;
	
	private String eventActionCode;
	
	private String eventDateTime;
	
	private String eventOutcomeIndicator;
	
	private String eventTypeCode;
	
	private AuditRequestSource auditRequestSource;
	
	private AuditRequestDestination auditRequestDestination;
	
	private AuditSource auditSource;
	
	private AuditQueryParameters auditQueryParameters;
	
	private List<AuditHumanRequestor> humanRequestors;

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

	public AuditRequestSource getAuditRequestSource() {
		return auditRequestSource;
	}

	public void setAuditRequestSource(AuditRequestSource auditRequestSource) {
		this.auditRequestSource = auditRequestSource;
	}

	public AuditRequestDestination getAuditRequestDestination() {
		return auditRequestDestination;
	}

	public void setAuditRequestDestination(
			AuditRequestDestination auditRequestDestination) {
		this.auditRequestDestination = auditRequestDestination;
	}

	public AuditSource getAuditSource() {
		return auditSource;
	}

	public void setAuditSource(AuditSource auditSource) {
		this.auditSource = auditSource;
	}

	public AuditQueryParameters getAuditQueryParameters() {
		return auditQueryParameters;
	}

	public void setAuditQueryParameters(AuditQueryParameters auditQueryParameters) {
		this.auditQueryParameters = auditQueryParameters;
	}

	public List<AuditHumanRequestor> getHumanRequestors() {
		return humanRequestors;
	}

	public void setHumanRequestors(List<AuditHumanRequestor> humanRequestors) {
		this.humanRequestors = humanRequestors;
	}
	
}
