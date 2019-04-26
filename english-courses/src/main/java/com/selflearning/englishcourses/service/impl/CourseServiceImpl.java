package com.selflearning.englishcourses.service.impl;

import com.selflearning.englishcourses.domain.Course;
import com.selflearning.englishcourses.domain.UserCourse;
import com.selflearning.englishcourses.repository.jpa.CourseJpaRepository;
import com.selflearning.englishcourses.repository.jpa.UserCourseJpaRepository;
import com.selflearning.englishcourses.service.CourseService;
import com.selflearning.englishcourses.service.dto.CourseDto;
import com.selflearning.englishcourses.service.dto.MyCourse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Service
public class CourseServiceImpl implements CourseService {

    private final CourseJpaRepository courseJpaRepository;
    private final UserCourseJpaRepository userCourseJpaRepository;
    private final ModelMapper modelMapper;


    @Override
    public Course get(UUID id) {
        return courseJpaRepository.findById(id).orElse(null);
    }

    @Override
    public Optional<Course> get(String name) {
        return courseJpaRepository.findByName(name);
    }

    @Override
    public List<MyCourse> getCoursesForUser(UUID userId) {
        return courseJpaRepository.findAllByUserId(userId);
    }

    @Override
    public void save(Course course) {
        courseJpaRepository.save(course);
    }

    @Transactional
    @Override
    public void saveAll(List<Course> courses) {
        courseJpaRepository.saveAll(courses);
    }

    @Override
    public void delete(UUID id) {
        courseJpaRepository.deleteById(id);
    }

    @Override
    public Page<Course> findAll(Pageable pageable) {
        return courseJpaRepository.findAll(pageable);
    }

    @Override
    public Course convertDtoToEntity(CourseDto courseDto) {
        return modelMapper.map(courseDto, Course.class);
    }

    @Override
    public List<Course> convertDtoToEntity(List<CourseDto> courseDtos) {
        return courseDtos.stream().map(this::convertDtoToEntity).collect(Collectors.toList());
    }

    @Override
    public CourseDto convertEntityToDto(Course entity) {
        return modelMapper.map(entity, CourseDto.class);
    }

    @Override
    public List<CourseDto> convertEntityToDto(List<Course> courses) {
        return courses.stream().map(this::convertEntityToDto).collect(Collectors.toList());
    }

    @Override
    public Page<CourseDto> convertEntityPageToDtoPage(Page<Course> page) {
        return new PageImpl<>(convertEntityToDto(page.getContent()), page.getPageable(), page.getNumberOfElements());
    }

    @Override
    public UserCourse getUserCourseByCourseIdAndUserId(UUID courseId, UUID userId) {
        return userCourseJpaRepository.findByCourseIdAndUserId(courseId, userId);
    }

    @Override
    public void createUserCourse(UserCourse userCourse) {
        userCourseJpaRepository.save(userCourse);
    }



}
