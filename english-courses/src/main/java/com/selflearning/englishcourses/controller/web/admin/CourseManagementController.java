package com.selflearning.englishcourses.controller.web.admin;

import com.selflearning.englishcourses.domain.*;
import com.selflearning.englishcourses.service.CourseService;
import com.selflearning.englishcourses.service.LessonModuleService;
import com.selflearning.englishcourses.service.LessonService;
import com.selflearning.englishcourses.service.VocabularyLessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Objects;
import java.util.UUID;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Controller
@RequestMapping("/admin/management")
public class CourseManagementController {

    private final CourseService courseService;

    private final LessonService lessonService;

    private final LessonModuleService lessonModuleService;

    private final VocabularyLessonService vocabularyLessonService;

    @GetMapping("/courses")
    public String coursePage(Model model) {
        model.addAttribute("courseManagement", true);
        return "admin/categories/courses";
    }

    @GetMapping("/courses/{id}/lessons")
    public String lessonPage(@PathVariable("id") UUID id, Model model) {
        Course course = courseService.get(id);
        Page<Lesson> lessonPage = lessonService.findByCourseId(course.getId(), PageRequest.of(0, 7));
        model.addAttribute("course", course);
        model.addAttribute("lessonPage", lessonPage);
        model.addAttribute("lessonManagement", true);
        return "admin/courses/lessons";
    }

    @GetMapping("/modules")
    public String modulePage(Model model) {
        model.addAttribute("moduleManagement", true);
        return "admin/categories/modules";
    }

    @GetMapping("/lesson-module/{lessonModuleId}")
    public String lessonModuleDetail(@PathVariable("lessonModuleId") UUID lessonModuleId, Model model) {
        LessonModule lessonModule = lessonModuleService.get(lessonModuleId);
        String name = lessonModule.getModule().getName();
        model.addAttribute("lessonModule", lessonModule);
        switch (name) {
            case "Luyện từ vựng":
                return "redirect:/admin/management/vocabulary-lessons/" + lessonModule.getVocabularyLesson().getId();
            case "Luyện ngữ pháp":
                return "admin/courses/lessons/grammar-lesson";
            case "Ngữ giao tiếp thông dụng":
                return "admin/courses/lessons/phrase-lesson";
            default:
                return "errors/404";
        }
    }

    @GetMapping("/vocabulary-lessons/{vocabularyLessonId}")
    public String vocabularyLesson(@PathVariable("vocabularyLessonId") UUID vocabularyLessonId, Model model) {
        VocabularyLesson vocabularyLesson = vocabularyLessonService.get(vocabularyLessonId);
        model.addAttribute("lessonManagement", true);
        model.addAttribute("vocabularyLesson", vocabularyLesson);
        return "admin/courses/lessons/vocabulary-lesson";
    }

    @ModelAttribute("courses")
    public Boolean activeCourseManagement() {
        return true;
    }

}
