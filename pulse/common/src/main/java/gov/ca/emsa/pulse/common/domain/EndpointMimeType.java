package gov.ca.emsa.pulse.common.domain;

public class EndpointMimeType {
	
	private Long id;
	private String mimeType;
	
	public EndpointMimeType(){}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}
	
}
