package gov.ca.emsa.pulse.broker.domain;

import gov.ca.emsa.pulse.broker.dto.DocumentDTO;

public class Document {
	private String id;
	private String name;
	private Long orgMapId;
	
	public Document() {}
	
	public Document(DocumentDTO dto) {
		this.id = dto.getId()+"";
		this.name = dto.getName();
		this.orgMapId = dto.getPatientOrgMapId();
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getOrgMapId() {
		return orgMapId;
	}
	public void setOrgMapId(Long orgMapId) {
		this.orgMapId = orgMapId;
	}
}
