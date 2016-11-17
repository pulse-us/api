package gov.ca.emsa.pulse.broker.entity;

import gov.ca.emsa.pulse.broker.audit.AuditHumanRequestor;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="audit_event")
public class AuditEventEntity {
	
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column( name = "id", nullable = false )
	private Long id;
	
	@Column(name="event_id")
	private String eventId;
	
	@Column(name="event_action_code")
	private String eventActionCode;
	
	@Column(name="event_date_time")
	private String eventDateTime;
	
	@Column( name = "event_outcome_indicator", insertable = false, updatable = false)
	private String eventOutcomeIndicator;
	
	@Column( name = "event_type_code", insertable = false, updatable = false)
	private String eventTypeCode;
	
	@Column( name = "audit_request_source_id", insertable = false, updatable = false)
	private Long auditRequestSourceId;
	
	@OneToOne
	@JoinColumn( name = "audit_request_source_id", insertable = false, updatable = false, nullable = false)
	private AuditRequestSourceEntity auditRequestSource;
	
	@Column( name = "audit_request_destination_id", insertable = false, updatable = false, nullable = false)
	private Long auditRequestDestinationId;
	
	@OneToOne
	@JoinColumn( name = "audit_request_destination_id", insertable = false, updatable = false, nullable = false)
	private AuditRequestDestinationEntity auditRequestDestination;
	
	@Column( name = "audit_source_id", insertable = false, updatable = false, nullable = false)
	private Long auditSourceId;
	
	@OneToOne
	@JoinColumn( name = "audit_source_id", insertable = false, updatable = false, nullable = false)
	private AuditSourceEntity auditSource;
	
	@Column( name = "audit_query_parameters_id", insertable = false, updatable = false, nullable = false)
	private Long auditQueryParametersId;
	
	@OneToOne
	@JoinColumn( name = "audit_query_parameters_id", insertable = false, updatable = false, nullable = false)
	private AuditQueryParametersEntity auditQueryParameters;
	
	@Column( name = "human_requestor_id", insertable = false, updatable = false)
	private Long humanRequestorId;
	
	@OneToMany
	@JoinColumn( name = "human_requestor_id", insertable = false, updatable = false)
	private AuditHumanRequestorEntity auditHumanRequestor;

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

	public Long getAuditRequestSourceId() {
		return auditRequestSourceId;
	}

	public void setAuditRequestSourceId(Long auditRequestSourceId) {
		this.auditRequestSourceId = auditRequestSourceId;
	}

	public AuditRequestSourceEntity getAuditRequestSource() {
		return auditRequestSource;
	}

	public void setAuditRequestSource(AuditRequestSourceEntity auditRequestSource) {
		this.auditRequestSource = auditRequestSource;
	}

	public Long getAuditRequestDestinationId() {
		return auditRequestDestinationId;
	}

	public void setAuditRequestDestinationId(Long auditRequestDestinationId) {
		this.auditRequestDestinationId = auditRequestDestinationId;
	}

	public AuditRequestDestinationEntity getAuditRequestDestination() {
		return auditRequestDestination;
	}

	public void setAuditRequestDestination(
			AuditRequestDestinationEntity auditRequestDestination) {
		this.auditRequestDestination = auditRequestDestination;
	}

	public Long getAuditSourceId() {
		return auditSourceId;
	}

	public void setAuditSourceId(Long auditSourceId) {
		this.auditSourceId = auditSourceId;
	}

	public AuditSourceEntity getAuditSource() {
		return auditSource;
	}

	public void setAuditSource(AuditSourceEntity auditSource) {
		this.auditSource = auditSource;
	}

	public Long getAuditQueryParametersId() {
		return auditQueryParametersId;
	}

	public void setAuditQueryParametersId(Long auditQueryParametersId) {
		this.auditQueryParametersId = auditQueryParametersId;
	}

	public AuditQueryParametersEntity getAuditQueryParameters() {
		return auditQueryParameters;
	}

	public void setAuditQueryParameters(
			AuditQueryParametersEntity auditQueryParameters) {
		this.auditQueryParameters = auditQueryParameters;
	}

	public Long getHumanRequestorId() {
		return humanRequestorId;
	}

	public void setHumanRequestorId(Long humanRequestorId) {
		this.humanRequestorId = humanRequestorId;
	}

	public AuditHumanRequestorEntity getAuditHumanRequestor() {
		return auditHumanRequestor;
	}

	public void setAuditHumanRequestor(AuditHumanRequestorEntity auditHumanRequestor) {
		this.auditHumanRequestor = auditHumanRequestor;
	}

}