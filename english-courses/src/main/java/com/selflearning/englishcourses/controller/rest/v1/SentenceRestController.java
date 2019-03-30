package com.selflearning.englishcourses.controller.rest.v1;

import com.selflearning.englishcourses.domain.Sentence;
import com.selflearning.englishcourses.service.SentenceService;
import com.selflearning.englishcourses.service.dto.SentenceDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class SentenceRestController {

    private SentenceService sentenceService;

    @Value("${path.audio}")
    private String audioPath;


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

    @PutMapping("/sentences/{id}")
    public ResponseEntity<SentenceDto> updateSentences(@PathVariable("id") UUID id, @RequestBody SentenceDto sentenceDto){
        Sentence sentence = sentenceService.convertDtoToEntity(sentenceDto);
        sentenceService.save(sentence);
        return new ResponseEntity<>(sentenceService.convertEntityToDto(sentence), HttpStatus.OK);
    }

    @GetMapping("/sentences")
    public ResponseEntity<Page<SentenceDto>> getSentences(Pageable pageable) {
        Page<Sentence> sentencePage = sentenceService.findAll(pageable);
        List<SentenceDto> sentenceDtos = sentenceService.convertEntityToDto(sentencePage.getContent());
        return new ResponseEntity<>(new PageImpl<>(sentenceDtos, pageable, sentencePage.getTotalElements()), HttpStatus.OK);
    }

    @GetMapping("/sentences/{id}")
    public ResponseEntity<SentenceDto> getSentence(@PathVariable String id) {
        Sentence sentence = sentenceService.get(id);
        return new ResponseEntity<>(sentenceService.convertEntityToDto(sentence), HttpStatus.OK);
    }

    @GetMapping(value = "/sentences", params = "search")
    public ResponseEntity<Page<SentenceDto>> searchSentence(@RequestParam("search") String text, Pageable pageable) {
        Page<Sentence> sentencePage = sentenceService.searchByText(text, pageable);
        List<SentenceDto> sentenceDtos = sentenceService.convertEntityToDto(sentencePage.getContent());
        return new ResponseEntity<>(new PageImpl<>(sentenceDtos, pageable, sentencePage.getTotalElements()), HttpStatus.OK);
    }

    @GetMapping("/sentences/{id}/audio")
    public ResponseEntity<byte[]> getAudio(@PathVariable String id) throws IOException {
        Sentence sentence = sentenceService.get(id);
        String audioPath = sentence.getAudioPath();
        File file = new File(this.audioPath + audioPath);
        Resource resource = new FileSystemResource(file);
        byte[] bytes = Files.readAllBytes(file.toPath());
        return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                .contentType(MediaTypeFactory.getMediaType(resource).orElse(MediaType.APPLICATION_OCTET_STREAM))
                .body(bytes);
    }


    @Autowired
    public void setSentenceService(SentenceService sentenceService) {
        this.sentenceService = sentenceService;
    }

}
