package com.selflearning.englishcourses.service;

import com.selflearning.englishcourses.domain.Sentence;
import com.selflearning.englishcourses.service.dto.SentenceDto;

import java.util.UUID;

public interface SentenceService extends BaseCurdService<Sentence, UUID>, ModelMapperService<Sentence, SentenceDto> {
}
