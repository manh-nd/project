package com.selflearning.englishcourses.service.impl;

import com.selflearning.englishcourses.domain.Lesson;
import com.selflearning.englishcourses.domain.LessonModule;
import com.selflearning.englishcourses.repository.jpa.CourseJpaRepository;
import com.selflearning.englishcourses.repository.jpa.LessonJpaRepository;
import com.selflearning.englishcourses.service.LessonModuleService;
import com.selflearning.englishcourses.service.LessonService;
import com.selflearning.englishcourses.service.dto.LessonDto;
import com.selflearning.englishcourses.service.dto.LessonModuleDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@Service
public class LessonServiceImpl implements LessonService {

    private final LessonJpaRepository lessonJpaRepository;
    private final CourseJpaRepository courseJpaRepository;
    private final ModelMapper modelMapper;
    private final LessonModuleService lessonModuleService;

    @Override
    public Lesson get(UUID id) {
        return lessonJpaRepository.findById(id).orElse(null);
    }

    @Override
    public void save(Lesson lesson) {
        lessonJpaRepository.save(lesson);
    }

    @Override
    public void delete(UUID id) {
        lessonJpaRepository.deleteById(id);
    }

    @Override
    public void delete(Lesson obj) {
        lessonJpaRepository.delete(obj);
    }

    @Override
    public Page<Lesson> findAll(Pageable pageable) {
        return lessonJpaRepository.findAll(pageable);
    }

    @Override
    public Lesson convertDtoToEntity(LessonDto lessonDto) {
        Lesson lesson = modelMapper.map(lessonDto, Lesson.class);
        UUID id = lessonDto.getId();
        if (Objects.nonNull(id)) {
            lesson.setId(id);
        }
        UUID courseId = lessonDto.getCourseId();
        if (Objects.nonNull(courseId)) {
            lesson.setCourse(courseJpaRepository.findById(courseId).orElse(null));
        }
        return lesson;
    }

    @Override
    public List<Lesson> convertDtoToEntity(List<LessonDto> lessonDtos) {
        return lessonDtos.stream().map(this::convertDtoToEntity).collect(Collectors.toList());
    }

    @Override
    public LessonDto convertEntityToDto(Lesson lesson) {
        LessonDto lessonDto = modelMapper.map(lesson, LessonDto.class);
        lessonDto.setId(lesson.getId());
        lessonDto.setCourseId(lesson.getCourse().getId());
        lessonDto.setCourseName(lesson.getCourse().getName());
        List<LessonModule> lessonModules = lesson.getLessonModules();
        if (Objects.nonNull(lessonModules)) {
            List<LessonModuleDto> lessonModuleDtos = lessonModuleService.convertEntityToDto(lessonModules);
            lessonDto.setLessonModules(lessonModuleDtos);
        }
        return lessonDto;
    }

    @Override
    public List<LessonDto> convertEntityToDto(List<Lesson> lessons) {
        return lessons.stream().map(this::convertEntityToDto).collect(Collectors.toList());
    }

    @Override
    public Page<LessonDto> convertEntityPageToDtoPage(Page<Lesson> page) {
        return new PageImpl<>(convertEntityToDto(page.getContent()), page.getPageable(), page.getTotalElements());
    }

    @Override
    public Optional<Lesson> findByLessonOrderNumberAndCourseId(Integer orderNumber, UUID courseId) {
        return lessonJpaRepository.findByLessonOrderNumberAndCourseId(orderNumber, courseId);
    }

    @Override
    public Page<Lesson> findByCourseId(UUID courseId, Pageable pageable) {
        return lessonJpaRepository.findByCourseId(courseId, pageable);
    }

    @Override
    public Integer getNextOrderNumber(UUID courseId) {
        return lessonJpaRepository.getNextOrderNumber(courseId);
    }
}
