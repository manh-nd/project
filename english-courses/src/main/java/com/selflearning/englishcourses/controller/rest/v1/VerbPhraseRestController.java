package com.selflearning.englishcourses.controller.rest.v1;

import com.selflearning.englishcourses.domain.VerbPhrase;
import com.selflearning.englishcourses.service.VerbPhraseService;
import com.selflearning.englishcourses.service.dto.VerbPhraseDto;
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
public class VerbPhraseRestController {

    private VerbPhraseService verbPhraseService;

    @GetMapping("/verb-phrases")
    public ResponseEntity<Page<VerbPhraseDto>> getVerbPhrases(Pageable pageable){
        Page<VerbPhrase> verbPhrasePage = verbPhraseService.findAll(pageable);
        List<VerbPhrase> content = verbPhrasePage.getContent();
        long total = verbPhrasePage.getTotalElements();
        Page<VerbPhraseDto> verbPhraseDtoPage = new PageImpl<>(verbPhraseService.convertEntityToDto(content), pageable, total);
        return new ResponseEntity<>(verbPhraseDtoPage, HttpStatus.OK);
    }

    @PostMapping("/verb-phrases/save-all")
    public ResponseEntity<List<VerbPhraseDto>> createVerbPhrases(@RequestBody List<VerbPhraseDto> verbPhraseDtos) {
        List<VerbPhrase> verbPhrases = verbPhraseService.convertDtoToEntity(verbPhraseDtos);
        verbPhraseService.saveAll(verbPhrases);
        return new ResponseEntity<>(verbPhraseService.convertEntityToDto(verbPhrases), HttpStatus.CREATED);
    }

    @Autowired
    public void setVerbPhraseService(VerbPhraseService verbPhraseService) {
        this.verbPhraseService = verbPhraseService;
    }

}
