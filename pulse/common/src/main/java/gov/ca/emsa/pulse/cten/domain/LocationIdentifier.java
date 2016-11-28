package gov.ca.emsa.pulse.cten.domain;

public class LocationIdentifier {
	private String use;
	private String system;
	private String value;
	private ResourceType type;
	
	public LocationIdentifier() {
	}

	public String getUse() {
		return use;
	}

	public void setUse(String use) {
		this.use = use;
	}

	public String getSystem() {
		return system;
	}

	public void setSystem(String system) {
		this.system = system;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public ResourceType getType() {
		return type;
	}

	public void setType(ResourceType type) {
		this.type = type;
	}
}
