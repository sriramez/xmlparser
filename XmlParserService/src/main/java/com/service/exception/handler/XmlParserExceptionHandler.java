package com.service.exception.handler;

import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.service.exception.XmlAddExployeeException;
import com.service.exception.XmlReadException;
import com.service.exception.XmlRemoveEmployeeException;
import com.service.exception.XmlValidationException;
import com.service.exception.XmlValidatorNotPresentException;
import com.service.exception.XmlWriteException;
import com.service.services.impl.XMLParserServiceImpl;

@ControllerAdvice
public class XmlParserExceptionHandler {

	private static final Logger logger = LoggerFactory.getLogger(XmlParserExceptionHandler.class);

	@ExceptionHandler(value = XmlAddExployeeException.class)
	public ResponseEntity<Object> exception(XmlAddExployeeException exception) {
		logger.error(exception.getStackTrace().toString());
		return new ResponseEntity<>("Exception occured while adding an employee:", HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(value = XmlReadException.class)
	public ResponseEntity<Object> exception(XmlReadException exception) {
		logger.error(exception.getStackTrace().toString());
		return new ResponseEntity<>("Exception Reading the Xml file:", HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(value = XmlRemoveEmployeeException.class)
	public ResponseEntity<Object> exception(XmlRemoveEmployeeException exception) {
		logger.error(exception.getStackTrace().toString());
		return new ResponseEntity<>("Exception occured while removing an employee", HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(value = XmlValidationException.class)
	public ResponseEntity<Object> exception(XmlValidationException exception) {
		logger.error(exception.getStackTrace().toString());
		return new ResponseEntity<>("Exception occured while validation input", HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(value = XmlWriteException.class)
	public ResponseEntity<Object> exception(XmlWriteException exception) {
		logger.error(exception.getStackTrace().toString());
		return new ResponseEntity<>("Exception occured while Writing output", HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(value = XmlValidatorNotPresentException.class)
	public ResponseEntity<Object> exception(XmlValidatorNotPresentException exception) {
		logger.error(exception.getStackTrace().toString());
		return new ResponseEntity<>("Validator not present", HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(value = Exception.class)
	public ResponseEntity<Object> exception(Exception exception) {
		return new ResponseEntity<>(exception.getLocalizedMessage(),HttpStatus.NOT_FOUND);
	}
}
