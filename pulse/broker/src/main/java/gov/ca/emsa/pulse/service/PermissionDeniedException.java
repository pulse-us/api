package gov.ca.emsa.pulse.service;

public class PermissionDeniedException extends Exception {
	private static final long serialVersionUID = 7744435732290136697L;

	public PermissionDeniedException() {
		super();
	}
	
	public PermissionDeniedException(String msg) {
		super(msg);
	}
}
