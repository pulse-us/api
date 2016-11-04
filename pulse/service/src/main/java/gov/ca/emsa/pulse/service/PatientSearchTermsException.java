package gov.ca.emsa.pulse.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "One of the following search parameters was blank or improperly formed: Name, Date of Birth, Gender")
public class PatientSearchTermsException extends RuntimeException {}
