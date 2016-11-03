package gov.ca.emsa.pulse.service;

import java.sql.SQLException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import gov.ca.emsa.pulse.common.domain.ErrorJSONObject;


@ControllerAdvice
public class ApiExceptionControllerAdvice {
	
	@ExceptionHandler(InvalidArgumentsException.class)
	public ResponseEntity<ErrorJSONObject> exception(InvalidArgumentsException e) {
		e.printStackTrace();
		return new ResponseEntity<ErrorJSONObject>(new ErrorJSONObject(e.getMessage()), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(SQLException.class)
	public ResponseEntity<ErrorJSONObject> exception(SQLException e) {
		e.printStackTrace();
		return new ResponseEntity<ErrorJSONObject>(new ErrorJSONObject(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorJSONObject> exception(Exception e) {
		e.printStackTrace();
		return new ResponseEntity<ErrorJSONObject>(new ErrorJSONObject(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
}
