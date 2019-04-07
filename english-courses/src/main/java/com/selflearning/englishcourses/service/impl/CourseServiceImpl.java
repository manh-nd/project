package com.selflearning.englishcourses.service.impl;

import com.selflearning.englishcourses.domain.Course;
import com.selflearning.englishcourses.repository.elasticsearch.CourseElasticsearchRepository;
import com.selflearning.englishcourses.repository.jpa.CourseJpaRepository;
import com.selflearning.englishcourses.service.CourseService;
import com.selflearning.englishcourses.service.dto.CourseDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements CourseService {

    private CourseJpaRepository courseJpaRepository;

    private CourseElasticsearchRepository courseElasticsearchRepository;

    private ModelMapper modelMapper;

    @Autowired
    public void setCourseElasticsearchRepository(CourseElasticsearchRepository courseElasticsearchRepository) {
        this.courseElasticsearchRepository = courseElasticsearchRepository;
    }

    @Autowired
    public void setCourseJpaRepository(CourseJpaRepository courseJpaRepository) {
        this.courseJpaRepository = courseJpaRepository;
    }

    @Autowired
    public void setModelMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public Course get(UUID id) {
        return courseJpaRepository.findById(id).orElse(null);
    }

    @Override
    public Course get(String id) {
        return null;
    }

    @Override
    public void save(Course obj) {
        courseJpaRepository.save(obj);
        courseElasticsearchRepository.save(obj);
    }

    @Override
    public void saveAll(Iterable<Course> iterable) {
        courseJpaRepository.saveAll(iterable);
        courseElasticsearchRepository.saveAll(iterable);
    }

    @Override
    public void delete(Course obj) {
        courseJpaRepository.delete(obj);
        courseElasticsearchRepository.delete(obj);
    }

    @Override
    public void deleteAll(Iterable<Course> iterable) {
        courseJpaRepository.deleteAll(iterable);
    }

    @Override
    public Page<Course> findAll(Pageable pageable) {
        return courseElasticsearchRepository.findAll(pageable);
    }

    @Override
    public Course convertDtoToEntity(CourseDto courseDto) {
        Course course = modelMapper.map(courseDto, Course.class);
        String id = courseDto.getId();
        if (Objects.nonNull(id))
            course.setId(UUID.fromString(id));
        return course;
    }

    @Override
    public List<Course> convertDtoToEntity(List<CourseDto> courseDtos) {
        return courseDtos.stream().map(this::convertDtoToEntity).collect(Collectors.toList());
    }

    @Override
    public CourseDto convertEntityToDto(Course entity) {
        CourseDto courseDto = modelMapper.map(entity, CourseDto.class);
        if (entity.getId() != null)
            courseDto.setId(entity.getId().toString());
        return courseDto;
    }

    @Override
    public List<CourseDto> convertEntityToDto(List<Course> courses) {
        return courses.stream().map(this::convertEntityToDto).collect(Collectors.toList());
    }

    @Override
    public Page<CourseDto> convertEntityPageToDtoPage(Page<Course> page) {
        return null;
    }


}
