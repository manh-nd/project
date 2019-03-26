package com.selflearning.englishcourses.controller.web.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class CategoryController {

    @GetMapping("/courses")
    public String coursePage(Model model) {
        model.addAttribute("courses", true);
        return "admin/categories/course/courses";
    }

    @GetMapping("/sentences")
    public String sentencePage(Model model) {
        model.addAttribute("sentences", true);
        return "admin/categories/sentence/sentences";
    }

    @GetMapping("/vocabularies")
    public String vocabularyPage(Model model) {
        model.addAttribute("vocabularies", true);
        return "admin/categories/vocabulary/vocabularies";
    }

    @GetMapping("/verb-phrases")
    public String verbPhrasePage(Model model) {
        model.addAttribute("verbPhrases", true);
        return "admin/categories/verb-phrase/verb-phrases";
    }

    @ModelAttribute("categories")
    public Boolean setActive() {
        return true;
    }

}
