package gov.ca.emsa.pulse.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import gov.ca.emsa.pulse.broker.domain.ErrorJSONObject;


@ControllerAdvice
public class ApiExceptionControllerAdvice {
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorJSONObject> exception(Exception e) {
		e.printStackTrace();
		return new ResponseEntity<ErrorJSONObject>(new ErrorJSONObject(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
}
