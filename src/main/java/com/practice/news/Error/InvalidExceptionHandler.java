package com.practice.news.Error;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@ControllerAdvice
@Controller
public class InvalidExceptionHandler extends ResponseEntityExceptionHandler {

	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
		System.out.println("Error Internal: " + ex.getMessage());

		return super.handleExceptionInternal(ex, body, headers, status, request);
	}

	@ExceptionHandler(Invalid.class)
	public ResponseEntity<Object> handleNewsNotFoundException(Invalid ex,
															  WebRequest request
	) {
		ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(),
				request.getDescription(false));
		System.out.println("Error: " + ex.getMessage());
		return handleExceptionInternal(ex, errorDetails,
				new HttpHeaders(), HttpStatus.CONFLICT, request);

	}
}
