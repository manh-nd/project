package com.selflearning.englishcourses.controller.rest.v1;

import com.selflearning.englishcourses.domain.GrammarLesson;
import com.selflearning.englishcourses.domain.GrammarQuestion;
import com.selflearning.englishcourses.service.GrammarLessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@RestController
@RequestMapping("/api/v1")
public class GrammarLessonRestController {

    private final GrammarLessonService grammarLessonService;

    @PutMapping("/grammar-lessons/{id}")
    public ResponseEntity<GrammarLesson> updateGrammarLesson(@PathVariable("id") UUID id, @RequestBody GrammarLesson grammarLesson) {
        GrammarLesson grammarLessonInDb = grammarLessonService.get(id);
        grammarLessonInDb.setTitle(grammarLesson.getTitle());
        grammarLessonInDb.setContent(grammarLesson.getContent());
        grammarLessonService.save(grammarLessonInDb);
        return new ResponseEntity<>(grammarLesson, HttpStatus.OK);
    }

    @GetMapping("/grammar-lessons/{id}/questions")
    public ResponseEntity<List<GrammarQuestion>> getGrammarQuestions(@PathVariable("id") UUID id) {
        GrammarLesson grammarLesson = grammarLessonService.get(id);
        return new ResponseEntity<>(grammarLesson.getQuestions(), HttpStatus.OK);
    }

    @PutMapping("/grammar-lesson/{id}/questions")
    public ResponseEntity<GrammarLesson> updateQuestions(@PathVariable("id") UUID id, @RequestBody List<GrammarQuestion> questions) {
        GrammarLesson grammarLesson = grammarLessonService.get(id);
        grammarLesson.setQuestions(questions);
        grammarLesson.getQuestions().forEach(question -> {
            question.setGrammarLesson(grammarLesson);
            question.getAnswers().forEach(answer -> {
                answer.setQuestion(question);
            });
        });
        grammarLessonService.save(grammarLesson);
        return new ResponseEntity<>(grammarLesson, HttpStatus.OK);
    }

}
