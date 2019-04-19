package com.selflearning.englishcourses.controller.advice;

import com.selflearning.englishcourses.exception.SimpleFieldErrorException;
import com.selflearning.englishcourses.service.error.SimpleFieldError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SimpleFieldErrorException.class)
    public ResponseEntity<SimpleFieldError> handleFieldError(SimpleFieldErrorException exception){
        return new ResponseEntity<>(exception.getSimpleFieldError(), HttpStatus.BAD_REQUEST);
    }

}