package gov.ca.emsa.pulse.broker.domain;

import gov.ca.emsa.pulse.broker.dto.OrganizationDTO;

/**
 * A class that has only the fields about an organization we might want to return to a 
 * user on the outside of this system. They do not need connection details!
 * @author kekey
 *
 */
public class OrganizationBase {
	
	private String name;
	private Long id;
	private Long organizationId;
	private boolean isActive;
	private String adapter;
	
	public OrganizationBase(){}
	
	public OrganizationBase(String name){
		this.name = name;
	}
	
	public OrganizationBase(OrganizationDTO dto) {
		this.id = dto.getId();
		this.organizationId = dto.getOrganizationId();
		this.name = dto.getName();
		this.isActive = dto.isActive();
		this.adapter = dto.getAdapter();
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public String getAdapter() {
		return adapter;
	}

	public void setAdapter(String adapter) {
		this.adapter = adapter;
	}
}
