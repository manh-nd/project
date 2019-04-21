package com.selflearning.englishcourses.controller.web;

import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class DictionaryController {

    @GetMapping("/dictionary")
    public String dictionaryPage(Model model) {
        model.addAttribute("currentId", 1);
        model.addAttribute("currentText", "dictionary.all");
        model.addAttribute("currentPlaceHolder", "dictionary.all.placeholder");
        model.addAttribute("search", "");
        return "dictionary";
    }

    @GetMapping("/dictionary/all")
    public String allDictionaryPage(@RequestParam(name = "search", defaultValue = "") String search, Model model) {
        model.addAttribute("currentId", 1);
        model.addAttribute("currentText", "dictionary.all");
        model.addAttribute("currentPlaceHolder", "dictionary.all.placeholder");
        model.addAttribute("search", search);
        return "dictionary";
    }

    @GetMapping("/dictionary/vocabulary")
    public String vocabularyDictionaryPage(@RequestParam(name = "search", defaultValue = "") String search, Model model) {
        model.addAttribute("currentId", 2);
        model.addAttribute("currentText", "dictionary.vocabulary");
        model.addAttribute("currentPlaceHolder", "dictionary.vocabulary.placeholder");
        model.addAttribute("search", search);
        return "dictionary";
    }

    @GetMapping("/dictionary/sentence")
    public String sentenceDictionaryPage(@RequestParam(name = "search", defaultValue = "") String search, Model model) {
        model.addAttribute("currentId", 3);
        model.addAttribute("currentText", "dictionary.sentence");
        model.addAttribute("currentPlaceHolder", "dictionary.sentence.placeholder");
        model.addAttribute("search", search);
        return "dictionary";
    }

    @GetMapping("/dictionary/phrase")
    public String phraseDictionaryPage(@RequestParam(name = "search", defaultValue = "") String search, Model model) {
        model.addAttribute("currentId", 4);
        model.addAttribute("currentText", "dictionary.phrase");
        model.addAttribute("currentPlaceHolder", "dictionary.phrase.placeholder");
        model.addAttribute("search", search);
        return "dictionary";
    }

    @ModelAttribute("dictionary")
    public Boolean activeDictionary() {
        return true;
    }

}
