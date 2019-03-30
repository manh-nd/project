package com.selflearning.englishcourses.repository.jpa;

import com.selflearning.englishcourses.domain.Word;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface WordJpaRepository extends JpaRepository<Word, UUID> {

    Word findByTextAndIpa(String text, String ipa);

}
