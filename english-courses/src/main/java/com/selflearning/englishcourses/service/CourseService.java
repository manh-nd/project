package com.selflearning.englishcourses.service;

import com.selflearning.englishcourses.domain.Course;
import com.selflearning.englishcourses.domain.UserCourse;
import com.selflearning.englishcourses.service.dto.CourseDto;
import com.selflearning.englishcourses.service.dto.MyCourse;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CourseService extends BaseCurdService<Course, UUID>,
        ModelMapperService<Course, CourseDto> {

    UserCourse getUserCourseByCourseIdAndUserId(UUID courseId, UUID userId);

    void createUserCourse(UserCourse userCourse);

    Optional<Course> get(String name);

    List<MyCourse> getCoursesForUser(UUID id);

}
