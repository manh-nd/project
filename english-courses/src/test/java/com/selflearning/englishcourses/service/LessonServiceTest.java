package com.selflearning.englishcourses.service;

import com.selflearning.englishcourses.domain.Lesson;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LessonServiceTest {

    @Autowired
    private LessonService lessonService;

    @Test
    public void testDeleteLesson(){
        Lesson lesson = lessonService.get(UUID.fromString("46467da6-a8b4-468d-ab5c-b74757335f62"));
        lessonService.delete(lesson);
    }
}
