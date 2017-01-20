package gov.ca.emsa.pulse.broker.adapter;

public class IheErrorException extends Exception {

	private static final long serialVersionUID = -2300690481163715L;

	public IheErrorException() {
		super();
	}
	
	public IheErrorException(String msg) {
		super(msg);
	}
}
