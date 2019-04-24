package com.selflearning.englishcourses.repository.jpa;

import com.selflearning.englishcourses.domain.Course;
import com.selflearning.englishcourses.domain.UserCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CourseJpaRepository extends JpaRepository<Course, UUID> {

    Optional<Course> findByName(String name);

    @Query("SELECT c FROM Course c JOIN c.userCourses uc WHERE uc.user.id = :userId")
    List<Course> findAllByUser(@Param("userId") UUID uuid);
}
