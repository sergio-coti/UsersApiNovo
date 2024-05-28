package br.com.cotiinformatica.application.handlers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import br.com.cotiinformatica.domain.exceptions.AccessDeniedException;

@ControllerAdvice
public class UnauthorizedHandler {

	@ExceptionHandler(AccessDeniedException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	@ResponseBody
	public List<String> errorHandler(AccessDeniedException e) {
		
		List<String> errors = new ArrayList<String>();
		errors.add(e.getMessage());
		return errors;
	}	
	
}
