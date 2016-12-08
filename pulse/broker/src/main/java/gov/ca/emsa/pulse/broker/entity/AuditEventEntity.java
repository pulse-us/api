package gov.ca.emsa.pulse.broker.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
	
	// see Audit_Message_Information.xls for Audit message requirements
	
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column( name = "id", nullable = false )
	private Long id;
	
	@Column(name="event_id", nullable = false)
	private String eventId; 
	
	@Column(name="event_action_code", nullable = false) 
	private String eventActionCode;
	
	@Column(name="event_date_time", nullable = false)
	private String eventDateTime;
	
	@Column( name = "event_outcome_indicator", nullable = false)
	private String eventOutcomeIndicator;
	
	@Column( name = "event_type_code", nullable = false)
	private String eventTypeCode;
	
	@Column( name = "audit_request_source_id", nullable = false)
	private Long auditRequestSourceId;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn( name = "audit_request_source_id", insertable = false, updatable = false)
	private AuditRequestSourceEntity auditRequestSource;
	
	@Column( name = "audit_request_destination_id", nullable = false)
	private Long auditRequestDestinationId;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn( name = "audit_request_destination_id", insertable = false, updatable = false)
	private AuditRequestDestinationEntity auditRequestDestination;
	
	@Column( name = "audit_source_id", nullable = false)
	private Long auditSourceId;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn( name = "audit_source_id", insertable = false, updatable = false)
	private AuditSourceEntity auditSource;
	
	@Column( name = "audit_query_parameters_id", nullable = false)
	private Long auditQueryParametersId;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn( name = "audit_query_parameters_id", insertable = false, updatable = false)
	private AuditQueryParametersEntity auditQueryParameters;
	
	@Column( name = "audit_patient_id", nullable = false)
	private Long auditPatientId;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn( name = "audit_patient_id", insertable = false, updatable = false)
	private AuditPatientEntity auditPatient;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "auditEventId")
	@Column( name = "audit_event_id")
	private List<AuditHumanRequestorEntity> auditHumanRequestor = new ArrayList<AuditHumanRequestorEntity>();
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "auditEventId")
	@Column( name = "audit_event_id")
	private List<AuditDocumentEntity> auditDocument = new ArrayList<AuditDocumentEntity>();

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

	public AuditRequestDestinationEntity getAuditRequestDestination() {
		return auditRequestDestination;
	}

	public void setAuditRequestDestination(
			AuditRequestDestinationEntity auditRequestDestination) {
		this.auditRequestDestination = auditRequestDestination;
	}

	public AuditSourceEntity getAuditSource() {
		return auditSource;
	}

	public void setAuditSource(AuditSourceEntity auditSource) {
		this.auditSource = auditSource;
	}

	public AuditQueryParametersEntity getAuditQueryParameters() {
		return auditQueryParameters;
	}

	public void setAuditQueryParameters(
			AuditQueryParametersEntity auditQueryParameters) {
		this.auditQueryParameters = auditQueryParameters;
	}

	public List<AuditHumanRequestorEntity> getAuditHumanRequestor() {
		return auditHumanRequestor;
	}

	public void setAuditHumanRequestor(
			ArrayList<AuditHumanRequestorEntity> auditHumanRequestor) {
		this.auditHumanRequestor = auditHumanRequestor;
	}

	public AuditPatientEntity getAuditPatient() {
		return auditPatient;
	}

	public void setAuditPatient(AuditPatientEntity auditPatient) {
		this.auditPatient = auditPatient;
	}

	public List<AuditDocumentEntity> getAuditDocument() {
		return auditDocument;
	}

	public void setAuditDocument(ArrayList<AuditDocumentEntity> auditDocument) {
		this.auditDocument = auditDocument;
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

}