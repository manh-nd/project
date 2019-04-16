package com.selflearning.englishcourses.repository;

import com.selflearning.englishcourses.domain.*;
import com.selflearning.englishcourses.repository.jpa.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class VocabularyLessonRepositoryTest {


    @Autowired
    private LessonModuleJpaRepository lessonModuleJpaRepository;

    @Autowired
    private LessonJpaRepository lessonJpaRepository;

    @Autowired
    private ModuleJpaRepository moduleJpaRepository;

    @Autowired
    private CourseJpaRepository courseJpaRepository;

    @Autowired
    private SentenceJpaRepository sentenceJpaRepository;

    @Autowired
    private VocabularyJpaRepository vocabularyJpaRepository;

    @Autowired
    private VocabularyLessonJpaRepository vocabularyLessonJpaRepository;

    @Test
    public void testCreateVocabularyLesson(){
        VocabularyLesson vocabularyLesson = new VocabularyLesson();
        Optional<Lesson> lesson = lessonJpaRepository.findByCourseAndOrderNumber(courseJpaRepository.findByName("Tiếng Anh giao tiếp 360").get(), 1);
        Optional<Module> module = moduleJpaRepository.findByName("Luyện từ vựng");
        Optional<LessonModule> lessonModule = lessonModuleJpaRepository.findByLessonAndModule(lesson.get(), module.get());
        vocabularyLesson.setLessonModule(lessonModule.get());
        List<VocabularyLessonDetail> vocabularyLessonDetails = new ArrayList<>();
        VocabularyLessonDetail vocabularyLessonDetail = new VocabularyLessonDetail();
        vocabularyLessonDetail.setSentence(sentenceJpaRepository.findByText("Call me when you've arrived there."));
        vocabularyLessonDetail.setVocabulary(vocabularyJpaRepository.findByWordText("call"));
        vocabularyLessonDetail.setVocabularyLesson(vocabularyLesson);
        vocabularyLessonDetails.add(vocabularyLessonDetail);
        vocabularyLesson.setVocabularyLessonDetails(vocabularyLessonDetails);
        vocabularyLessonJpaRepository.save(vocabularyLesson);
    }

}
