package com.selflearning.englishcourses.controller.web;

import com.selflearning.englishcourses.domain.Course;
import com.selflearning.englishcourses.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Controller
public class HomeController {

    private final CourseService courseService;

    @GetMapping("/")
    public String index(Model model) {
        List<Course> courses = courseService.findAll(PageRequest.of(0, 10)).getContent();
        if (!courses.isEmpty())
            model.addAttribute("courses", courses);
        model.addAttribute("home", true);
        return "index";
    }

    @GetMapping("/default")
    public String defaultAfterLogin(Authentication authentication) {
        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            return "redirect:/admin/dashboard";
        }
        return "redirect:/";
    }

}
