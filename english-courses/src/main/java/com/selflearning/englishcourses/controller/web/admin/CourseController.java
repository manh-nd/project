package com.selflearning.englishcourses.controller.web.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/management")
public class CourseController {

    @GetMapping("/courses")
    public String coursePage(Model model) {
        model.addAttribute("courseManagement", true);
        return "admin/courses/course/courses";
    }

    @ModelAttribute("courses")
    public Boolean activeCourseManagement(){
        return true;
    }

}
