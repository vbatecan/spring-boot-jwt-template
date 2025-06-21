package com.vbatecan.portfolio_manager.controllers;

import com.vbatecan.portfolio_manager.models.output.MessageResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandlerController {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
		try {
			Map<String, String> errors = new HashMap<>();
			ex.getBindingResult().getAllErrors().forEach(error -> {
				String fieldName = (( FieldError ) error).getField();
				String errorMessage = error.getDefaultMessage();
				errors.put(fieldName, errorMessage);
			});
			return ResponseEntity.badRequest().body(errors);
		} catch ( ClassCastException e ) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@ExceptionHandler(AuthorizationDeniedException.class)
	public ResponseEntity<?> handleAuthorizationDeniedException(AuthorizationDeniedException ex) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Please authenticate.", false));
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<?> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
		return ResponseEntity.badRequest().body(new MessageResponse(ex.getMessage(), false));
	}

	@ExceptionHandler(MissingServletRequestParameterException.class)
	public ResponseEntity<?> handleMissingServletRequestParameterException(MissingServletRequestParameterException ex) {
		return ResponseEntity.badRequest().body(new MessageResponse(ex.getMessage(), false));
	}

//	@ExceptionHandler(Exception.class)
//	public ResponseEntity<?> handleGenericException(Exception ex) {
//		return ResponseEntity.badRequest().body(new MessageResponse(ex.getMessage(), false));
//	}
}
