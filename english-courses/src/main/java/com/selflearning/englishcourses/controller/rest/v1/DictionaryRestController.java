package com.selflearning.englishcourses.controller.rest.v1;

import com.selflearning.englishcourses.domain.Vocabulary;
import com.selflearning.englishcourses.service.DictionaryService;
import com.selflearning.englishcourses.service.VocabularyService;
import com.selflearning.englishcourses.service.dto.DictionaryDto;
import com.selflearning.englishcourses.service.dto.PhraseDto;
import com.selflearning.englishcourses.service.dto.SentenceDto;
import com.selflearning.englishcourses.service.dto.VocabularyDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;
import java.util.UUID;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/api/v1")
public class DictionaryRestController {

    private final DictionaryService dictionaryService;

    private final VocabularyService vocabularyService;

    @Value("${base-path}")
    private String basePath;

    @GetMapping(value = "/dictionary/all")
    public ResponseEntity<DictionaryDto> search(@RequestParam(name = "search", defaultValue = "") String keyword) {
        return new ResponseEntity<>(dictionaryService.searchAll(keyword), HttpStatus.OK);
    }

    @GetMapping(value = "/dictionary/vocabulary")
    public ResponseEntity<Page<VocabularyDto>> searchVocabulary(
            @RequestParam(name = "search", defaultValue = "") String keyword, Pageable pageable) {
        return new ResponseEntity<>(dictionaryService.searchVocabulary(keyword, pageable), HttpStatus.OK);
    }

    @GetMapping(value = "/dictionary/phrase")
    public ResponseEntity<Page<PhraseDto>> searchVerbPhrase(
            @RequestParam(name = "search", defaultValue = "") String keyword, Pageable pageable) {
        return new ResponseEntity<>(dictionaryService.searchPhrase(keyword, pageable), HttpStatus.OK);
    }

    @GetMapping(value = "/dictionary/sentence")
    public ResponseEntity<Page<SentenceDto>> searchSentence(
            @RequestParam(name = "search", defaultValue = "") String keyword, Pageable pageable) {
        return new ResponseEntity<>(dictionaryService.searchSentence(keyword, pageable), HttpStatus.OK);
    }


    @GetMapping("/dictionary/vocabularies/{id}/audio")
    public ResponseEntity<byte[]> getVocabularyImage(@PathVariable("id") UUID id) throws IOException {
        Vocabulary vocabulary = vocabularyService.get(id);
        String audioPath = vocabulary.getWord().getNormalAudioPath();
        if (Objects.isNull(audioPath) || audioPath.trim().isEmpty()) {
            audioPath = vocabulary.getWord().getSpecialAudioPath();
        }
        File file = new File(this.basePath + audioPath);
        Resource resource = new FileSystemResource(file);
        byte[] bytes = Files.readAllBytes(file.toPath());
        return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                .contentType(MediaTypeFactory.getMediaType(resource).orElse(MediaType.APPLICATION_OCTET_STREAM))
                .body(bytes);
    }
}
