package gov.ca.emsa.pulse.common.domain;

public class AddressLine {
	private Long id;
	private String line;
	
	public AddressLine(){}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLine() {
		return line;
	}

	public void setLine(String line) {
		this.line = line;
	}
}
