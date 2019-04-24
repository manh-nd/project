package com.selflearning.englishcourses.controller.web;

import com.selflearning.englishcourses.domain.Course;
import com.selflearning.englishcourses.domain.User;
import com.selflearning.englishcourses.domain.UserCourse;
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
import java.util.Objects;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Controller
public class HomeController {

    private final CourseService courseService;

    @GetMapping("/")
    public String index(Model model, Authentication authentication) {
        List<Course> courses;
        if (authentication != null) {
            User user = (User) authentication.getPrincipal();
            courses = courseService.getCoursesByUserId(user.getId());
        } else {
            courses = courseService.findAll(PageRequest.of(0, 10)).getContent();
        }
        model.addAttribute("home", true);
        model.addAttribute("courses", courses);
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
