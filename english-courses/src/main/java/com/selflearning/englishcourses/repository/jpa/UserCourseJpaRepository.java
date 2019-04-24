package com.selflearning.englishcourses.repository.jpa;

import com.selflearning.englishcourses.domain.UserCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface UserCourseJpaRepository extends JpaRepository<UserCourse, UUID> {

    @Query("SELECT uc FROM UserCourse uc WHERE uc.user.id = :userId")
    UserCourse findByUserId(UUID userId);
}
