package gov.ca.emsa.pulse.common.domain;

public class ErrorJSONObject {
	
	String error;

	public ErrorJSONObject(String errorMessage){
		error = errorMessage;
	}
	
	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

}
