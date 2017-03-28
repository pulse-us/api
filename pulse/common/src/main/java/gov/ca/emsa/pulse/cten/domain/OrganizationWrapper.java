package gov.ca.emsa.pulse.cten.domain;

import java.util.ArrayList;
import java.util.List;

public class OrganizationWrapper {
	private String type;
	private Integer total;
	private String resourceType;
	private List<Link> link;
	private List<Organization> entry;
	
	public OrganizationWrapper() {
		link = new ArrayList<Link>();
		entry = new ArrayList<Organization>();
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public String getResourceType() {
		return resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	public List<Link> getLink() {
		return link;
	}

	public void setLink(List<Link> link) {
		this.link = link;
	}

	public List<Organization> getEntry() {
		return entry;
	}

	public void setEntry(List<Organization> entry) {
		this.entry = entry;
	}
}
