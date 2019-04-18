package com.selflearning.englishcourses.controller.rest.v1;

import com.selflearning.englishcourses.domain.LessonModule;
import com.selflearning.englishcourses.service.LessonModuleService;
import com.selflearning.englishcourses.service.ModuleService;
import com.selflearning.englishcourses.service.dto.LessonModuleDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class LessonModuleRestController {

    private LessonModuleService lessonModuleService;

    private ModuleService moduleService;

    @Autowired
    public void setLessonModuleService(LessonModuleService lessonModuleService) {
        this.lessonModuleService = lessonModuleService;
    }

    @Autowired
    public void setModuleService(ModuleService moduleService) {
        this.moduleService = moduleService;
    }

    @PostMapping("/lesson-modules")
    public ResponseEntity<LessonModuleDto> createLessonModule(@Valid @RequestBody LessonModuleDto lessonModuleDto) {
        LessonModule lessonModule = lessonModuleService.convertDtoToEntity(lessonModuleDto);
        lessonModuleService.save(lessonModule);
        return new ResponseEntity<>(lessonModuleService.convertEntityToDto(lessonModule), HttpStatus.CREATED);
    }

}
