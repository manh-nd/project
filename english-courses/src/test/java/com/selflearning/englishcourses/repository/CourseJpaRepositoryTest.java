package com.selflearning.englishcourses.repository;

import com.selflearning.englishcourses.repository.jpa.CourseJpaRepository;
import com.selflearning.englishcourses.service.dto.MyCourse;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CourseJpaRepositoryTest {

    @Autowired
    private CourseJpaRepository courseJpaRepository;

    @Test
    public void testCourse(){
        List<MyCourse> courses = courseJpaRepository.findAllByUserId(UUID.fromString("fdd02878-a8ef-412a-a013-185b9da8c880"));
        Assert.assertNotNull(courses);
    }

}
