package com.selflearning.englishcourses.service;

import com.selflearning.englishcourses.domain.Word;

import java.util.UUID;

public interface WordService extends BaseCurdService<Word, UUID>, ModelMapperService<Word, UUID> {
}
