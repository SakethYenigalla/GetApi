package com.example.getApi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControlAdvice {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        System.out.println("Exception occurred: " + e.getMessage());
        return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
    }
}
