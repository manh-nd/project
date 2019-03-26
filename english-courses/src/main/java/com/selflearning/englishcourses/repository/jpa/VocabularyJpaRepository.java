package com.selflearning.englishcourses.repository.jpa;

import com.selflearning.englishcourses.domain.Vocabulary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VocabularyJpaRepository extends JpaRepository<Vocabulary, UUID> {
}
