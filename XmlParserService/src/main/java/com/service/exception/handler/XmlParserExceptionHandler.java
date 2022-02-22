package com.service.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.service.exception.XmlAddExployeeException;
import com.service.exception.XmlReadException;
import com.service.exception.XmlRemoveEmployeeException;
import com.service.exception.XmlValidationException;
import com.service.exception.XmlWriteException;

@ControllerAdvice
public class XmlParserExceptionHandler {

	@ExceptionHandler(value = XmlAddExployeeException.class)
	public ResponseEntity<Object> exception(XmlAddExployeeException exception) {
		return new ResponseEntity<>("Exception occured while adding an employee:", HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(value = XmlReadException.class)
	public ResponseEntity<Object> exception(XmlReadException exception) {
		return new ResponseEntity<>("Exception Reading the Xml file:", HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(value = XmlRemoveEmployeeException.class)
	public ResponseEntity<Object> exception(XmlRemoveEmployeeException exception) {
		return new ResponseEntity<>("Exception occured while removing an employee", HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(value = XmlValidationException.class)
	public ResponseEntity<Object> exception(XmlValidationException exception) {
		return new ResponseEntity<>("Exception occured while validation input", HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(value = XmlWriteException.class)
	public ResponseEntity<Object> exception(XmlWriteException exception) {
		return new ResponseEntity<>("Exception occured while Writing output", HttpStatus.NOT_FOUND);
	}

}
