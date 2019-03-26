package com.selflearning.englishcourses.service;

import com.selflearning.englishcourses.domain.Vocabulary;
import com.selflearning.englishcourses.service.dto.VocabularyDto;

import java.util.UUID;

public interface VocabularyService extends BaseCurdService<Vocabulary, UUID>, ModelMapperService<Vocabulary, VocabularyDto> {
}
