package gov.ca.emsa.pulse.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "One of the following fields were blank or incorrect: DOB, Family/Given Name, Gender")
public class PatientSearchTermsException extends RuntimeException {}
