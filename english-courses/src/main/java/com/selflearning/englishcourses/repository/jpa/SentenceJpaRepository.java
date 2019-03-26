package com.selflearning.englishcourses.repository.jpa;

import com.selflearning.englishcourses.domain.Sentence;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SentenceJpaRepository extends JpaRepository<Sentence, UUID> {
}
