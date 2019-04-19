package com.selflearning.englishcourses.service.error;

import lombok.Data;

@Data
public class SimpleFieldError {

    private String key;
    private String message;

}
