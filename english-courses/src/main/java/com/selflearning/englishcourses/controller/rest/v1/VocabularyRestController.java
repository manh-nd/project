package com.selflearning.englishcourses.controller.rest.v1;

import com.selflearning.englishcourses.domain.Vocabulary;
import com.selflearning.englishcourses.service.VocabularyService;
import com.selflearning.englishcourses.service.dto.VocabularyDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class VocabularyRestController {

    @Autowired
    private VocabularyService vocabularyService;

    @PostMapping("/vocabularies/save-all")
    public ResponseEntity<List<VocabularyDto>> saveAll(@RequestBody List<VocabularyDto> vocabularyDtos){
        List<Vocabulary> vocabularies = vocabularyService.convertDtoToEntity(vocabularyDtos);
        vocabularyService.saveAll(vocabularies);
        vocabularyDtos = vocabularyService.convertEntityToDto(vocabularies);
        return new ResponseEntity<>(vocabularyDtos, HttpStatus.CREATED);
    }
}
