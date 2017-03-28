package gov.ca.emsa.pulse.service;

public class AcfChangesNotAllowedException extends Exception {
	private static final long serialVersionUID = 7744435732290136697L;

	public AcfChangesNotAllowedException() {
		super();
	}
	
	public AcfChangesNotAllowedException(String msg) {
		super(msg);
	}
}
