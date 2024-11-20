package com.udemy.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<?> handleValidationException(MethodArgumentNotValidException ex) {
    String errorMessage = ex.getBindingResult()
        .getFieldErrors()
        .stream()
        .map(error -> error.getDefaultMessage())
        .findFirst()
        .orElse("Error de validación");

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
  }
}
