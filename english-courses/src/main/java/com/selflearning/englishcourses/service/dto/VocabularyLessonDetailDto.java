package com.selflearning.englishcourses.service.dto;

import com.selflearning.englishcourses.domain.Sentence;
import com.selflearning.englishcourses.domain.Vocabulary;
import lombok.Data;

import java.util.UUID;

@Data
public class VocabularyLessonDetailDto extends BaseDto{

    private UUID sentenceId;
    private UUID vocabularyId;
    private UUID vocabularyLessonId;
    private Vocabulary vocabulary;
    private Sentence sentence;

}
