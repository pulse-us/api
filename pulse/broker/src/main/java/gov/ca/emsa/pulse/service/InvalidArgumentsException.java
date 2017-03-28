package gov.ca.emsa.pulse.service;

public class InvalidArgumentsException extends Exception {
	private static final long serialVersionUID = 7744435732290136697L;

	public InvalidArgumentsException() {
		super();
	}
	
	public InvalidArgumentsException(String msg) {
		super(msg);
	}
}
