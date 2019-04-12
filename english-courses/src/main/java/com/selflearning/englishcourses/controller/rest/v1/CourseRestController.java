package com.selflearning.englishcourses.controller.rest.v1;

import com.selflearning.englishcourses.domain.Course;
import com.selflearning.englishcourses.service.CourseService;
import com.selflearning.englishcourses.service.dto.CourseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class CourseRestController {

    private CourseService courseService;

    @Autowired
    public void setCourseService(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping("/courses")
    public ResponseEntity<CourseDto> createCourse(@RequestBody CourseDto courseDto){
        Course course = courseService.convertDtoToEntity(courseDto);
        courseService.save(course);
        courseDto = courseService.convertEntityToDto(course);
        return new ResponseEntity<>(courseDto, HttpStatus.CREATED);
    }

    @PostMapping("/courses/all")
    public ResponseEntity<List<CourseDto>> createCourses(@RequestBody List<CourseDto> courseDtos){
        List<Course> courses = courseService.convertDtoToEntity(courseDtos);
        courseService.saveAll(courses);
        courseDtos = courseService.convertEntityToDto(courses);
        return new ResponseEntity<>(courseDtos, HttpStatus.CREATED);
    }

    @GetMapping("/courses")
    public ResponseEntity<Page<CourseDto>> findAll(Pageable pageable){
        Page<Course> coursePage = courseService.findAll(pageable);
        return new ResponseEntity<>(courseService.convertEntityPageToDtoPage(coursePage), HttpStatus.OK);
    }

}
