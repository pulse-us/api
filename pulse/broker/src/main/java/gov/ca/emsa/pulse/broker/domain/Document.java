package gov.ca.emsa.pulse.broker.domain;

public class Document {
	private String id;
	private String name;
	private Long orgMapId;
	
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
