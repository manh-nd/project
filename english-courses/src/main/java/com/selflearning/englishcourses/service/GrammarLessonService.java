package com.selflearning.englishcourses.service;

import com.selflearning.englishcourses.domain.GrammarLesson;
import com.selflearning.englishcourses.domain.GrammarQuestion;

import java.util.List;
import java.util.UUID;

public interface GrammarLessonService extends BaseCurdService<GrammarLesson, UUID> {

    void removeQuestions(List<GrammarQuestion> grammarQuestions);

}
