package com.selflearning.englishcourses.service.impl;

import com.selflearning.englishcourses.domain.GrammarLesson;
import com.selflearning.englishcourses.domain.GrammarQuestion;
import com.selflearning.englishcourses.repository.jpa.GrammarLessonJpaRepository;
import com.selflearning.englishcourses.repository.jpa.GrammarQuestionJpaRepository;
import com.selflearning.englishcourses.service.GrammarLessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Service
public class GrammarLessonServiceImpl implements GrammarLessonService {

    private final GrammarLessonJpaRepository grammarLessonJpaRepository;

    private final GrammarQuestionJpaRepository grammarQuestionJpaRepository;

    @Override
    public GrammarLesson get(UUID id) {
        return grammarLessonJpaRepository.findById(id).orElse(null);
    }

    @Override
    public void save(GrammarLesson obj) {
        grammarLessonJpaRepository.save(obj);
    }

    @Override
    public Page<GrammarLesson> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public void removeQuestions(List<GrammarQuestion> grammarQuestions) {
        grammarQuestionJpaRepository.deleteAll(grammarQuestions);
    }

}
