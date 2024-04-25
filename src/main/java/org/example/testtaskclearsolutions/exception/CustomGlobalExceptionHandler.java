package org.example.testtaskclearsolutions.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(
			MethodArgumentNotValidException ex,
			HttpHeaders headers,
			HttpStatusCode status,
			WebRequest request) {
		Map<String, Object> body = new LinkedHashMap<>();
		List<String> errors = ex.getBindingResult().getAllErrors().stream()
				.map(this::getErrorMessage)
				.toList();
		body.put("errors", errors);
		return new ResponseEntity<>(body, headers, status);
	}

	@ExceptionHandler
	protected ResponseEntity<Object> handleRegistrationException(RegistrationException ex) {
		ErrorResponseWrapper errorResponseWrapper = new ErrorResponseWrapper(
				ex.getMessage(),
				LocalDateTime.now()
		);
		return new ResponseEntity<>(errorResponseWrapper, CONFLICT);
	}

	@ExceptionHandler
	protected ResponseEntity<Object> handleNoSuchElementException(EntityNotFoundException ex) {
		ErrorResponseWrapper errorResponseWrapper = new ErrorResponseWrapper(
				ex.getMessage(),
				LocalDateTime.now()
		);
		return new ResponseEntity<>(errorResponseWrapper, NOT_FOUND);
	}


	private String getErrorMessage(ObjectError e) {
		if (e instanceof FieldError) {
			String field = ((FieldError) e).getField();
			String message = e.getDefaultMessage();
			return field + " " + message;
		}
		return e.getDefaultMessage();
	}
}
