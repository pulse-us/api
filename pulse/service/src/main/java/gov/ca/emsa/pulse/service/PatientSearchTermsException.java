package gov.ca.emsa.pulse.service;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "One of the follwing fields were blank: DOB, Family/Given Name, Gender")
public class PatientSearchTermsException extends RuntimeException {}
