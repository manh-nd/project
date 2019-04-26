package com.selflearning.englishcourses.service.error;

import lombok.Data;

@Data
public class SimpleFieldError {

    private final String key;
    private final String message;

}
