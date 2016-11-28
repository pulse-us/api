package gov.ca.emsa.pulse.cten.domain;

import java.util.ArrayList;
import java.util.List;

public class ResourceType {
	private String text;
	private List<ResourceCoding> coding;
	
	public ResourceType() {
		this.coding = new ArrayList<ResourceCoding>();
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public List<ResourceCoding> getCoding() {
		return coding;
	}
	public void setCoding(List<ResourceCoding> coding) {
		this.coding = coding;
	}
}
