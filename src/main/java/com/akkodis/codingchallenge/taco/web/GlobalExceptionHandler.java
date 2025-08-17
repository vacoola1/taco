package com.akkodis.codingchallenge.taco.web;

import com.akkodis.codingchallenge.taco.service.exceptions.NotEnoughAmountException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({NotEnoughAmountException.class})
    public ResponseEntity<Object> notEnoughAmount(NotEnoughAmountException exception) {
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                .body(exception.getMessage());
    }

    @ExceptionHandler({EntityNotFoundException.class})
    public ResponseEntity<Object> notFound(EntityNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(exception.getMessage());
    }

    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<Object> other(RuntimeException exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
    }
}
