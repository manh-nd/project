package com.selflearning.englishcourses.controller.rest.v1;

import com.selflearning.englishcourses.service.DictionaryService;
import com.selflearning.englishcourses.service.dto.DictionaryDto;
import com.selflearning.englishcourses.service.dto.SentenceDto;
import com.selflearning.englishcourses.service.dto.VerbPhraseDto;
import com.selflearning.englishcourses.service.dto.VocabularyDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class DictionaryRestController {

    private DictionaryService dictionaryService;

    @Autowired
    public void setDictionaryService(DictionaryService dictionaryService) {
        this.dictionaryService = dictionaryService;
    }

    @GetMapping(value = "/dictionary")
    public ResponseEntity<DictionaryDto> search(@RequestParam("search") String keyword) {
        return new ResponseEntity<>(dictionaryService.searchAll(keyword), HttpStatus.OK);
    }

    @GetMapping(value = "/dictionary", params = "vocabulary")
    public ResponseEntity<Page<VocabularyDto>> searchVocabulary(@RequestParam("search") String keyword, Pageable pageable) {
        return new ResponseEntity<>(dictionaryService.searchVocabulary(keyword, pageable), HttpStatus.OK);
    }

    @GetMapping(value = "/dictionary", params = "verb-phrase")
    public ResponseEntity<Page<VerbPhraseDto>> searchVerbPhrase(@RequestParam("search") String keyword, Pageable pageable) {
        return new ResponseEntity<>(dictionaryService.searchVerbPhrase(keyword, pageable), HttpStatus.OK);
    }

    @GetMapping(value = "/dictionary", params = "sentence")
    public ResponseEntity<Page<SentenceDto>> searchSentence(@RequestParam("search") String keyword, Pageable pageable) {
        return new ResponseEntity<>(dictionaryService.searchSentence(keyword, pageable), HttpStatus.OK);
    }


}
