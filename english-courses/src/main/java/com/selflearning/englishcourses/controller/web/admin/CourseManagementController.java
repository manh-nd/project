package com.selflearning.englishcourses.controller.web.admin;

import com.selflearning.englishcourses.domain.*;
import com.selflearning.englishcourses.service.*;
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

import java.util.UUID;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Controller
@RequestMapping("/admin/management")
public class CourseManagementController {

    private final CourseService courseService;

    private final LessonService lessonService;

    private final LessonModuleService lessonModuleService;

    private final VocabularyLessonService vocabularyLessonService;

    private final GrammarLessonService grammarLessonService;

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
        model.addAttribute("courseManagement", true);
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
        model.addAttribute("courseManagement", true);
        switch (name) {
            case "Luyện từ vựng":
                return "redirect:/admin/management/vocabulary-lessons/" + lessonModule.getVocabularyLesson().getId();
            case "Luyện ngữ pháp":
                return "redirect:/admin/management/grammar-lessons/" + lessonModule.getGrammarLesson().getId();
            case "Ngữ giao tiếp thông dụng":
                return "admin/courses/lessons/phrase-lesson";
            default:
                return "errors/404";
        }
    }

    @GetMapping("/vocabulary-lessons/{vocabularyLessonId}")
    public String vocabularyLesson(@PathVariable("vocabularyLessonId") UUID vocabularyLessonId, Model model) {
        VocabularyLesson vocabularyLesson = vocabularyLessonService.get(vocabularyLessonId);
        model.addAttribute("vocabularyLesson", vocabularyLesson);
        model.addAttribute("courseManagement", true);
        return "admin/courses/lessons/vocabulary-lesson";
    }

    @GetMapping("/grammar-lessons/{grammarLessonId}")
    public String grammarLesson(@PathVariable("grammarLessonId") UUID grammarLessonId, Model model) {
        GrammarLesson grammarLesson = grammarLessonService.get(grammarLessonId);
        model.addAttribute("courseManagement", true);
        model.addAttribute("grammarLesson", grammarLesson);
        return "admin/courses/lessons/grammar-lesson";
    }

    @GetMapping("/grammar-lessons/{grammarLessonId}/quiz")
    public String grammarLessonQuiz(@PathVariable("grammarLessonId") UUID grammarLessonId, Model model) {
        GrammarLesson grammarLesson = grammarLessonService.get(grammarLessonId);
        model.addAttribute("courseManagement", true);
        model.addAttribute("grammarLesson", grammarLesson);
        return "admin/courses/lessons/grammar-lesson-quiz";
    }

    @ModelAttribute("courses")
    public Boolean activeCourseManagement() {
        return true;
    }

}
