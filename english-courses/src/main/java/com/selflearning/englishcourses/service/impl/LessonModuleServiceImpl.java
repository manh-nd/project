package com.selflearning.englishcourses.service.impl;

import com.selflearning.englishcourses.domain.*;
import com.selflearning.englishcourses.repository.jpa.CourseJpaRepository;
import com.selflearning.englishcourses.repository.jpa.LessonModuleJpaRepository;
import com.selflearning.englishcourses.repository.jpa.LessonModuleMarkJpaRepository;
import com.selflearning.englishcourses.service.CourseService;
import com.selflearning.englishcourses.service.LessonModuleService;
import com.selflearning.englishcourses.service.LessonService;
import com.selflearning.englishcourses.service.ModuleService;
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
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@Service
public class LessonModuleServiceImpl implements LessonModuleService {

    private final LessonModuleJpaRepository lessonModuleJpaRepository;

    private final LessonModuleMarkJpaRepository lessonModuleMarkJpaRepository;

    @Autowired
    private LessonService lessonService;

    private final ModuleService moduleService;

    private final ModelMapper modelMapper;

    @Override
    public LessonModule get(UUID id) {
        return lessonModuleJpaRepository.findById(id).orElse(null);
    }

    @Override
    public void save(LessonModule lessonModule) {
        lessonModuleJpaRepository.save(lessonModule);
    }

    @Override
    public void delete(UUID id) {
        lessonModuleJpaRepository.deleteById(id);
    }

    @Override
    public Page<LessonModule> findAll(Pageable pageable) {
        return lessonModuleJpaRepository.findAll(pageable);
    }

    @Override
    public LessonModule convertDtoToEntity(LessonModuleDto lessonModuleDto) {
        LessonModule lessonModule = modelMapper.map(lessonModuleDto, LessonModule.class);
        UUID lessonId = lessonModuleDto.getLessonId();
        if (Objects.nonNull(lessonId)) {
            Lesson lesson = lessonService.get(lessonId);
            lessonModule.setLesson(lesson);
        }
        UUID moduleId = lessonModuleDto.getModuleId();
        if (Objects.nonNull(moduleId)) {
            Module module = moduleService.get(moduleId);
            lessonModule.setModule(module);
        }
        return lessonModule;
    }

    @Override
    public List<LessonModule> convertDtoToEntity(List<LessonModuleDto> lessonModuleDtos) {
        return lessonModuleDtos.stream().map(this::convertDtoToEntity).collect(Collectors.toList());
    }

    @Override
    public LessonModuleDto convertEntityToDto(LessonModule lessonModule) {
        LessonModuleDto lessonModuleDto = modelMapper.map(lessonModule, LessonModuleDto.class);
        lessonModuleDto.setLessonId(lessonModule.getLesson().getId());
        lessonModuleDto.setCourseId(lessonModule.getLesson().getCourse().getId());
        lessonModuleDto.setOrderNumber(lessonModule.getLesson().getOrderNumber());
        lessonModuleDto.setModuleId(lessonModule.getModule().getId());
        lessonModuleDto.setModuleName(lessonModule.getModule().getName());
        return lessonModuleDto;
    }

    @Override
    public List<LessonModuleDto> convertEntityToDto(List<LessonModule> lessonModules) {
        return lessonModules.stream().map(this::convertEntityToDto).collect(Collectors.toList());
    }

    @Override
    public Page<LessonModuleDto> convertEntityPageToDtoPage(Page<LessonModule> lessonModulePage) {
        return new PageImpl<>(convertEntityToDto(lessonModulePage.getContent()),
                lessonModulePage.getPageable(), lessonModulePage.getTotalElements());
    }

    @Override
    public void createLessonModuleMark(LessonModuleMark lessonModuleMark) {
        lessonModuleMarkJpaRepository.save(lessonModuleMark);
    }
}
