package com.selflearning.englishcourses.service;

import com.selflearning.englishcourses.domain.Course;
import com.selflearning.englishcourses.domain.UserCourse;
import com.selflearning.englishcourses.service.dto.CourseDto;

import java.util.List;
import java.util.UUID;

public interface CourseService extends BaseCurdService<Course, UUID>,
        ModelMapperService<Course, CourseDto> {

    UserCourse getUserCourseByUserId(UUID userId);

    void createUserCourse(UserCourse userCourse);

    List<Course> getCoursesByUserId(UUID userId);

}
