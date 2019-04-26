package com.selflearning.englishcourses.repository.jpa;

import com.selflearning.englishcourses.domain.Course;
import com.selflearning.englishcourses.domain.UserCourse;
import com.selflearning.englishcourses.service.dto.MyCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CourseJpaRepository extends JpaRepository<Course, UUID> {

    Optional<Course> findByName(String name);

    @Query("SELECT new com.selflearning.englishcourses.service.dto.MyCourse(c, CASE WHEN EXISTS(SELECT 1 FROM UserCourse uc WHERE uc.user.id = :userId AND uc.course.id = c.id)THEN true ELSE false END) FROM Course c")
    List<MyCourse> findAllByUserId(@Param("userId") UUID userId);


}
