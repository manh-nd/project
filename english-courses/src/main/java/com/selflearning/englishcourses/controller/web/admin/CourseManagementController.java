package com.selflearning.englishcourses.controller.web.admin;

import com.selflearning.englishcourses.domain.GrammarLesson;
import com.selflearning.englishcourses.domain.LessonModule;
import com.selflearning.englishcourses.domain.VocabularyLesson;
import com.selflearning.englishcourses.service.LessonModuleService;
import com.selflearning.englishcourses.service.VocabularyLessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Objects;
import java.util.UUID;

@Controller
@RequestMapping("/admin/management")
public class CourseManagementController {

    @Autowired
    private LessonModuleService lessonModuleService;

    @Autowired
    private VocabularyLessonService vocabularyLessonService;

    @GetMapping("/courses")
    public String coursePage(Model model) {
        model.addAttribute("courseManagement", true);
        return "admin/categories/courses";
    }

    @GetMapping("/lessons")
    public String lessonPage(Model model) {
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
    public String vocabularyLesson(@PathVariable("vocabularyLessonId") UUID vocabularyLessonId, Model model){
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
