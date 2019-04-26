package com.selflearning.englishcourses.service.dto;

import com.selflearning.englishcourses.domain.Course;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MyCourse {

    private Course course;
    private Boolean exists;


}
