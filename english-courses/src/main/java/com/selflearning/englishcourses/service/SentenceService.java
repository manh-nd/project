package com.selflearning.englishcourses.service;

import com.selflearning.englishcourses.domain.Sentence;
import com.selflearning.englishcourses.service.dto.SentenceDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface SentenceService extends BaseCurdService<Sentence, UUID>, ModelMapperService<Sentence, SentenceDto> {

    void updateSentenceAudioPath();

    Page<Sentence> searchByText(String text, Pageable pageable);
}
