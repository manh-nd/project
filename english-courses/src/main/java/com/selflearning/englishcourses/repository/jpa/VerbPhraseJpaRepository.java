package com.selflearning.englishcourses.repository.jpa;

import com.selflearning.englishcourses.domain.VerbPhrase;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VerbPhraseJpaRepository extends JpaRepository<VerbPhrase, UUID> {
}
