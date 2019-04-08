package com.selflearning.englishcourses.controller.web.admin;

import com.selflearning.englishcourses.controller.rest.v1.SentenceRestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/management")
public class CategoryController {

    @GetMapping("/sentences")
    public String sentencePage(Model model) {
        model.addAttribute("sentenceManagement", true);
        return "admin/categories/sentence/sentences";
    }

    @GetMapping("/sentences/page/{number}")
    public String sentencePage(Model model, @PathVariable("number") Integer number) {
        return sentencePage(model);
    }

    @GetMapping("/vocabularies")
    public String vocabularyPage(Model model) {
        model.addAttribute("vocabularyManagement", true);
        return "admin/categories/vocabulary/vocabularies";
    }

    @GetMapping("/vocabularies/page/{page}")
    public String vocabularyPage(Model model, @PathVariable("page") Integer page) {
        return vocabularyPage(model);
    }

    @GetMapping("/phrases")
    public String phrasePage(Model model) {
        model.addAttribute("phraseManagement", true);
        return "admin/categories/phrase/phrases";
    }

    @GetMapping("/phrases/page/{page}/")
    public String phrasePage(Model model, @PathVariable("page") Integer page) {
        return phrasePage(model);
    }

    @ModelAttribute("categories")
    public Boolean setActive() {
        return true;
    }

}
