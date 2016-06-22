package gov.ca.emsa.pulse.broker.dto;

import gov.ca.emsa.pulse.broker.entity.PatientQueryResultEntity;

public class PatientQueryResultDTO {

	private Long id;
	private Long queryOrgId;
	private Long patientId;
	private PatientDTO patient;
	private QueryOrganizationDTO queryOrg;
	
	public PatientQueryResultDTO(){}
	
	public PatientQueryResultDTO(PatientQueryResultEntity entity)
	{
		if(entity != null) {
			this.id = entity.getId();
			this.queryOrgId = entity.getQueryOrganizationId();
			this.patientId = entity.getPatientId();
			if(entity.getPatient() != null) {
				this.patient = new PatientDTO(entity.getPatient());
			}
			if(entity.getQueryOrg() != null) {
				this.queryOrg = new QueryOrganizationDTO(entity.getQueryOrg());
			}
		}
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public Long getQueryOrgId() {
		return queryOrgId;
	}

	public void setQueryOrgId(Long queryOrgId) {
		this.queryOrgId = queryOrgId;
	}

	public Long getPatientId() {
		return patientId;
	}

	public void setPatientId(Long patientId) {
		this.patientId = patientId;
	}

	public PatientDTO getPatient() {
		return patient;
	}

	public void setPatient(PatientDTO patient) {
		this.patient = patient;
	}

	public QueryOrganizationDTO getQueryOrg() {
		return queryOrg;
	}

	public void setQueryOrg(QueryOrganizationDTO queryOrg) {
		this.queryOrg = queryOrg;
	}
}
