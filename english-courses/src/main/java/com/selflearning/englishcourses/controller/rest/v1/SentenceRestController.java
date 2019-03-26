package com.selflearning.englishcourses.controller.rest.v1;

import com.selflearning.englishcourses.domain.Sentence;
import com.selflearning.englishcourses.service.SentenceService;
import com.selflearning.englishcourses.service.dto.SentenceDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class SentenceRestController {

    private SentenceService sentenceService;

    @PostMapping("/sentences")
    public ResponseEntity<SentenceDto> createSentence(@RequestBody SentenceDto sentenceDto) {
        Sentence sentence = sentenceService.convertDtoToEntity(sentenceDto);
        sentenceService.save(sentence);
        return new ResponseEntity<>(sentenceService.convertEntityToDto(sentence), HttpStatus.CREATED);
    }

    @PostMapping("/sentences/save-all")
    public ResponseEntity<List<SentenceDto>> createSentences(@RequestBody List<SentenceDto> sentenceDtos) {
        List<Sentence> sentences = sentenceService.convertDtoToEntity(sentenceDtos);
        sentenceService.saveAll(sentences);
        return new ResponseEntity<>(sentenceService.convertEntityToDto(sentences), HttpStatus.CREATED);
    }

    @GetMapping("/sentences")
    public ResponseEntity<Page<SentenceDto>> getSentences(Pageable pageable) {
        Page<Sentence> sentencePage = sentenceService.findAll(pageable);
        List<SentenceDto> sentenceDtos = sentenceService.convertEntityToDto(sentencePage.getContent());
        return new ResponseEntity<>(new PageImpl<>(sentenceDtos, pageable, sentencePage.getTotalElements()), HttpStatus.OK);
    }

    @GetMapping("/sentences/{id}")
    public ResponseEntity<SentenceDto> getSentence(@PathVariable String id){
        Sentence sentence = sentenceService.get(id);
        return new ResponseEntity<>(sentenceService.convertEntityToDto(sentence), HttpStatus.OK);
    }

    @Autowired
    public void setSentenceService(SentenceService sentenceService) {
        this.sentenceService = sentenceService;
    }
}
