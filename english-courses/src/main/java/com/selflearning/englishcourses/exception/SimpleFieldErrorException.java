package com.selflearning.englishcourses.exception;

import com.selflearning.englishcourses.service.error.SimpleFieldError;

public class SimpleFieldErrorException extends RuntimeException {

    private final SimpleFieldError simpleFieldError;

    public SimpleFieldErrorException(SimpleFieldError simpleFieldError) {
        this.simpleFieldError = simpleFieldError;
    }

    public SimpleFieldError getSimpleFieldError() {
        return simpleFieldError;
    }

}
