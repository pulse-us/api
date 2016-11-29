package gov.ca.emsa.pulse.broker.dto;

import gov.ca.emsa.pulse.broker.entity.AuditSourceEntity;

public class AuditSourceDTO {
	
	private Long id;
	
	private String auditSourceId;
	
	private String auditEnterpriseSiteId;
	
	private String auditSourceTypeCode;
	
	public AuditSourceDTO(){
		
	}
	
	public AuditSourceDTO(AuditSourceEntity entity) {
		this.auditSourceId = entity.getAuditSourceId();
		this.auditEnterpriseSiteId = entity.getAuditEnterpriseSiteId();
		this.auditSourceTypeCode = entity.getAuditSourceTypeCode();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAuditSourceId() {
		return auditSourceId;
	}

	public void setAuditSourceId(String auditSourceId) {
		this.auditSourceId = auditSourceId;
	}

	public String getAuditEnterpriseSiteId() {
		return auditEnterpriseSiteId;
	}

	public void setAuditEnterpriseSiteId(String auditEnterpriseSiteId) {
		this.auditEnterpriseSiteId = auditEnterpriseSiteId;
	}

	public String getAuditSourceTypeCode() {
		return auditSourceTypeCode;
	}

	public void setAuditSourceTypeCode(String auditSourceTypeCode) {
		this.auditSourceTypeCode = auditSourceTypeCode;
	}
	
}
