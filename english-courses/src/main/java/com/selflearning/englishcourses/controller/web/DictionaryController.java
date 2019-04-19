package com.selflearning.englishcourses.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class DictionaryController {

    @GetMapping("/dictionary")
    public String dictionaryPage(){
        return "dictionary";
    }

    @ModelAttribute("dictionary")
    public Boolean activeDictionary(){
        return true;
    }

}
