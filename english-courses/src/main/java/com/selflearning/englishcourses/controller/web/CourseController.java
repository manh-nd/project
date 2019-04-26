package com.selflearning.englishcourses.controller.web;

import com.selflearning.englishcourses.domain.*;
import com.selflearning.englishcourses.service.CourseService;
import com.selflearning.englishcourses.service.LessonModuleService;
import com.selflearning.englishcourses.service.LessonService;
import com.selflearning.englishcourses.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Controller
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;

    private final LessonService lessonService;

    private final LessonModuleService lessonModuleService;

    @GetMapping("/{id}")
    public String getCourse(@PathVariable("id") UUID id, @RequestParam(name = "week", defaultValue = "1") Integer week, Model model) {
        Course course = courseService.get(id);
        if (--week < 0) {
            week = 0;
        }
        Page<Lesson> lessonPage = lessonService.findByCourseId(id, week);
        model.addAttribute("course", course);
        model.addAttribute("lessonPage", lessonPage);
        return "courses/course-detail";
    }

    @PostMapping("/{id}")
    public String joinUserCourse(@PathVariable("id") UUID id, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        UserCourse userCourse = courseService.getUserCourseByCourseIdAndUserId(id, user.getId());
        if (Objects.isNull(userCourse)) {
            userCourse = new UserCourse();
            Course course = courseService.get(id);
            userCourse.setCourse(course);
            userCourse.setUser(user);
            userCourse.setJoinedTime(new Date());
            courseService.createUserCourse(userCourse);
            return "redirect:/courses/" + id;
        }
        return "redirect:/";
    }

    @GetMapping("/{id}/lessons/{lessonId}")
    public String getLesson(@PathVariable("id") UUID courseId, @PathVariable("lessonId") UUID lessonId, Model model) {
        Lesson lesson = lessonService.get(lessonId);
        List<LessonModule> lessonModules = lesson.getLessonModules();
        model.addAttribute("courseId", courseId);
        model.addAttribute("lessonId", lessonId);
        model.addAttribute("lesson", lesson);
        model.addAttribute("lessonModules", lessonModules);
        Integer orderNumber = lesson.getOrderNumber();
        int week = (int) Math.floor((double) orderNumber / 7 + 0.9);
        model.addAttribute("week", week);
        return "courses/lessons";
    }

    @GetMapping("/{id}/lessons/{lessonId}/modules/{lessonModuleId}")
    public String getLessonDetail(
            @PathVariable("id") UUID courseId,
            @PathVariable("lessonId") UUID lessonId,
            @PathVariable("lessonModuleId") UUID lessonModuleId,
            Model model) {
        LessonModule lessonModule = lessonModuleService.get(lessonModuleId);
        Module module = lessonModule.getModule();
        Integer orderNumber = lessonModule.getLesson().getOrderNumber();
        int week = (int) Math.floor((double) orderNumber / 7 + 0.9);
        model.addAttribute("courseId", courseId);
        model.addAttribute("lessonId", lessonId);
        model.addAttribute("lessonModuleId", lessonModuleId);
        model.addAttribute("week", week);
        switch (module.getName()) {
            case "Luyện từ vựng":
                VocabularyLesson vocabularyLesson = lessonModule.getVocabularyLesson();
                model.addAttribute("vocabularyLesson", vocabularyLesson);
                return "/courses/lessons/vocabulary";
            case "Luyện ngữ pháp":
                GrammarLesson grammarLesson = lessonModule.getGrammarLesson();
                model.addAttribute("grammarLesson", grammarLesson);
                return "courses/lessons/grammar";
            default:
                return "errors/404";
        }
    }

    @GetMapping("/{id}/lessons/{lessonId}/modules/{lessonModuleId}/quiz")
    public String grammarQuiz(
            @PathVariable("id") UUID courseId,
            @PathVariable("lessonId") UUID lessonId,
            @PathVariable("lessonModuleId") UUID lessonModuleId,
            Model model) {
        LessonModule lessonModule = lessonModuleService.get(lessonModuleId);
        GrammarLesson grammarLesson = lessonModule.getGrammarLesson();
        Integer orderNumber = lessonModule.getLesson().getOrderNumber();
        int week = (int) Math.floor((double) orderNumber / 7 + 0.9);
        model.addAttribute("courseId", courseId);
        model.addAttribute("lessonId", lessonId);
        model.addAttribute("lessonModuleId", lessonModuleId);
        model.addAttribute("grammarLesson", grammarLesson);
        model.addAttribute("week", week);
        return "/courses/lessons/grammar-quiz";
    }


}
