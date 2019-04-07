package com.selflearning.englishcourses.service;

import com.selflearning.englishcourses.domain.VerbPhrase;
import com.selflearning.englishcourses.service.dto.DictionaryDto;
import com.selflearning.englishcourses.service.dto.SentenceDto;
import com.selflearning.englishcourses.service.dto.VocabularyDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DictionaryService {

    DictionaryDto searchAll(String keyword);

    Page<SentenceDto> searchSentence(String keyword, Pageable pageable);

    Page<VocabularyDto> searchVocabulary(String keyword, Pageable pageable);

    Page<VerbPhrase> searchVerbPhrase(String keyword, Pageable pageable);

}
