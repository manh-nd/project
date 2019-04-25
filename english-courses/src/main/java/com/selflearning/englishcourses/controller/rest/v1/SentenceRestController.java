package com.selflearning.englishcourses.controller.rest.v1;

import com.selflearning.englishcourses.domain.Sentence;
import com.selflearning.englishcourses.service.SentenceService;
import com.selflearning.englishcourses.service.dto.SentenceDto;
import com.selflearning.englishcourses.util.StringUtils;
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
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Sentence Rest API
 *
 * @author manhnd
 */
@RestController
@RequestMapping("/api/v1")
public class SentenceRestController {

    private SentenceService sentenceService;

    @Autowired
    public void setSentenceService(SentenceService sentenceService) {
        this.sentenceService = sentenceService;
    }

    @Value("${base-path}")
    private String path;

    @PostMapping("/sentences")
    public ResponseEntity<SentenceDto> createSentence(
            @RequestPart("audioFile") @Nullable MultipartFile audioFile,
            @Valid @RequestPart("sentence") SentenceDto sentenceDto) throws IOException {
        Sentence sentence = sentenceService.convertDtoToEntity(sentenceDto);
        saveAndSetAudioPath(audioFile, sentence);
        sentenceService.save(sentence);
        return new ResponseEntity<>(sentenceService.convertEntityToDto(sentence), HttpStatus.CREATED);
    }

    private void saveAndSetAudioPath(MultipartFile audioFile, Sentence sentence) throws IOException {
        if (Objects.nonNull(audioFile) && !audioFile.isEmpty()) {
            Resource resource = audioFile.getResource();
            String fileName = StringUtils.formatToFileName(sentence.getText(), "mp3");
            String audioPath = "/Audios/DictionarySentences/" + fileName;
            Path path = Paths.get(this.path + audioPath);
            Files.copy(resource.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            sentence.setAudioPath(audioPath);
        }
    }

    @PostMapping("/sentences/save-all")
    public ResponseEntity<List<SentenceDto>> createSentences(@RequestBody List<SentenceDto> sentenceDtos) {
        List<Sentence> sentences = sentenceService.convertDtoToEntity(sentenceDtos);
        sentenceService.saveAll(sentences);
        return new ResponseEntity<>(sentenceService.convertEntityToDto(sentences), HttpStatus.CREATED);
    }

    @PutMapping("/sentences/{id}")
    public ResponseEntity<SentenceDto> updateSentence(
            @PathVariable("id") UUID id,
            @RequestPart("audioFile") @Nullable MultipartFile audioFile,
            @RequestPart("sentence") @Valid SentenceDto sentenceDto) throws IOException {
        Sentence sentence = sentenceService.get(id);
        sentence.setText(sentenceDto.getText());
        sentence.setIpa(sentenceDto.getIpa());
        sentence.setMeaning(sentenceDto.getMeaning());
        saveAndSetAudioPath(audioFile, sentence);
        sentenceService.save(sentence);
        return new ResponseEntity<>(sentenceService.convertEntityToDto(sentence), HttpStatus.OK);
    }

    @GetMapping("/sentences")
    public ResponseEntity<Page<SentenceDto>> getSentences(Pageable pageable) {
        return new ResponseEntity<>(sentenceService.convertEntityPageToDtoPage(sentenceService.findAll(pageable)), HttpStatus.OK);
    }

    @GetMapping("/sentences/{id}")
    public ResponseEntity<SentenceDto> getSentence(@PathVariable UUID id) {
        Sentence sentence = sentenceService.get(id);
        return new ResponseEntity<>(sentenceService.convertEntityToDto(sentence), HttpStatus.OK);
    }

    @GetMapping(value = "/sentences", params = "search")
    public ResponseEntity<Page<SentenceDto>> searchSentence(@RequestParam("search") String value, Pageable pageable) {
        return new ResponseEntity<>(sentenceService.convertEntityPageToDtoPage(sentenceService.search(value, pageable)),
                HttpStatus.OK);
    }

    @GetMapping("/sentences/{id}/audio")
    public ResponseEntity<byte[]> getAudio(@PathVariable UUID id) throws IOException {
        Sentence sentence = sentenceService.get(id);
        String audioPath = sentence.getAudioPath();
        File file = new File(this.path + audioPath);
        Resource resource = new FileSystemResource(file);
        byte[] bytes = Files.readAllBytes(file.toPath());
        return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                .contentType(MediaTypeFactory.getMediaType(resource).orElse(MediaType.APPLICATION_OCTET_STREAM))
                .body(bytes);
    }

    @GetMapping("/sentences/count")
    public ResponseEntity<Long> getTotalSentences() {
        return new ResponseEntity<>(sentenceService.count(), HttpStatus.OK);
    }

}
