package com.selflearning.englishcourses.service.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class CourseDto {

    private String id;
    private String name;
    private Date createdTime;
    private Date updatedTime;

}
