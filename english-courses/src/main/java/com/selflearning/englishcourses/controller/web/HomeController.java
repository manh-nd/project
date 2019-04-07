package com.selflearning.englishcourses.controller.web;

import com.selflearning.englishcourses.domain.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@Controller
public class HomeController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/default")
    public String defaultAfterLogin(HttpServletRequest request){
        if(request.isUserInRole("ROLE_ADMIN")){
            return "redirect:/admin/dashboard";
        }
        return "redirect:/";
    }


    @GetMapping("/403")
    public String accessDenyPage(){
        return "error/403";
    }

}
