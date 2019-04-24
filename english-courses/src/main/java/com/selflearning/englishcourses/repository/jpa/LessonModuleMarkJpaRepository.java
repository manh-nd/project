package com.selflearning.englishcourses.repository.jpa;

import com.selflearning.englishcourses.domain.LessonModuleMark;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LessonModuleMarkJpaRepository extends JpaRepository<LessonModuleMark, UUID> {
}
