package com.selflearning.englishcourses.repository.jpa;

import com.selflearning.englishcourses.domain.VocabularyLessonDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VocabularyLessonDetailJpaRepository extends JpaRepository<VocabularyLessonDetail, UUID> {
}
