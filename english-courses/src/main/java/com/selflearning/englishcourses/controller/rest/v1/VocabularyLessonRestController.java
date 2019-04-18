package com.selflearning.englishcourses.controller.rest.v1;

import com.selflearning.englishcourses.domain.Sentence;
import com.selflearning.englishcourses.domain.Vocabulary;
import com.selflearning.englishcourses.domain.VocabularyLesson;
import com.selflearning.englishcourses.domain.VocabularyLessonDetail;
import com.selflearning.englishcourses.service.SentenceService;
import com.selflearning.englishcourses.service.VocabularyLessonService;
import com.selflearning.englishcourses.service.VocabularyService;
import com.selflearning.englishcourses.service.dto.VocabularyLessonDetailDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@RestController
@RequestMapping("/api/v1")
public class VocabularyLessonRestController {

    private final VocabularyLessonService vocabularyLessonService;

    private final VocabularyService vocabularyService;

    private final SentenceService sentenceService;

    @GetMapping("/vocabulary-lessons/{vocabularyLessonId}")
    public ResponseEntity<VocabularyLesson> getVocabularyLesson(@PathVariable UUID vocabularyLessonId) {
        return new ResponseEntity<>(vocabularyLessonService.get(vocabularyLessonId), HttpStatus.OK);
    }

    @DeleteMapping("/vocabulary-lessons/{vocabularyLessonId}/{vocabularyLessonDetailId}")
    public ResponseEntity<VocabularyLesson> deleteVocabularyLessonDetailById(
            @PathVariable("vocabularyLessonId") UUID vocabularyLessonId,
            @PathVariable("vocabularyLessonDetailId") UUID vocabularyLessonDetailId) {
        vocabularyLessonService.deleteVocabularyLessonDetailById(vocabularyLessonDetailId);
        return new ResponseEntity<>(vocabularyLessonService.get(vocabularyLessonId), HttpStatus.OK);
    }

    @PutMapping("/vocabulary-lessons/{vocabularyLessonId}")
    public ResponseEntity<VocabularyLesson> updateVocabularyLessons(
            @PathVariable("vocabularyLessonId") UUID vocabularyLessonId,
            @RequestBody VocabularyLessonDetailDto vocabularyLessonDetailDto) {

        // Get vocabulary lesson
        VocabularyLesson vocabularyLesson = vocabularyLessonService.get(vocabularyLessonId);
        List<VocabularyLessonDetail> vocabularyLessonDetails = vocabularyLesson.getVocabularyLessonDetails();

        if (Objects.isNull(vocabularyLessonDetails))
            vocabularyLessonDetails = new ArrayList<>();

        VocabularyLessonDetail vocabularyLessonDetail = new VocabularyLessonDetail();

        // Sentence
        Sentence sentence = new Sentence();
        sentence.setId(vocabularyLessonDetailDto.getSentenceId());
        vocabularyLessonDetail.setSentence(sentence);

        // Sentence
        Vocabulary vocabulary = new Vocabulary();
        vocabulary.setId(vocabularyLessonDetailDto.getVocabularyId());
        vocabularyLessonDetail.setVocabulary(vocabulary);
        vocabularyLessonDetail.setVocabularyLesson(vocabularyLesson);

        vocabularyLessonDetails.add(vocabularyLessonDetail);
        vocabularyLessonService.save(vocabularyLesson);

        return new ResponseEntity<>(vocabularyLessonService.get(vocabularyLessonId), HttpStatus.OK);
    }

}
