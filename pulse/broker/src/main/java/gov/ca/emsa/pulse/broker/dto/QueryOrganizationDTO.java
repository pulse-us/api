package gov.ca.emsa.pulse.broker.dto;

import gov.ca.emsa.pulse.broker.entity.PatientRecordEntity;
import gov.ca.emsa.pulse.broker.entity.QueryOrganizationEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class QueryOrganizationDTO {

	private Long id;
	private Long queryId;
	private Long orgId;
	private OrganizationDTO org;
	private String status;
	private Date startDate;
	private Date endDate;
	private Boolean success;
	private List<PatientRecordDTO> results;
	
	public QueryOrganizationDTO(){
		results = new ArrayList<PatientRecordDTO>();
	}
	
	public QueryOrganizationDTO(QueryOrganizationEntity entity)
	{
		this();
		if(entity != null) {
			this.id = entity.getId();
			this.queryId = entity.getQueryId();
			this.orgId = entity.getOrganizationId();
			if(entity.getOrg() != null) {
				this.org = new OrganizationDTO(entity.getOrg());
			}
			
			this.status = entity.getStatus();
			this.startDate = entity.getStartDate();
			this.endDate = entity.getEndDate();
			this.success = entity.getSuccess();
			
			if(entity.getResults() != null && entity.getResults().size() > 0) {
				for(PatientRecordEntity pr : entity.getResults()) {
					PatientRecordDTO dto = new PatientRecordDTO(pr);
					this.results.add(dto);
				}
			}
		}
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public Long getQueryId() {
		return queryId;
	}

	public void setQueryId(Long queryId) {
		this.queryId = queryId;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public List<PatientRecordDTO> getResults() {
		return results;
	}

	public void setResults(List<PatientRecordDTO> results) {
		this.results = results;
	}

	public OrganizationDTO getOrg() {
		return org;
	}

	public void setOrg(OrganizationDTO org) {
		this.org = org;
	}
}