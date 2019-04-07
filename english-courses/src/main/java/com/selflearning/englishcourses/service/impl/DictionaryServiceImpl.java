package com.selflearning.englishcourses.service.impl;

import com.selflearning.englishcourses.domain.Sentence;
import com.selflearning.englishcourses.domain.VerbPhrase;
import com.selflearning.englishcourses.service.DictionaryService;
import com.selflearning.englishcourses.service.SentenceService;
import com.selflearning.englishcourses.service.dto.DictionaryDto;
import com.selflearning.englishcourses.service.dto.SentenceDto;
import com.selflearning.englishcourses.service.dto.VocabularyDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DictionaryServiceImpl implements DictionaryService {

    private SentenceService sentenceService;

    @Autowired
    public void setSentenceService(SentenceService sentenceService) {
        this.sentenceService = sentenceService;
    }

    @Override
    public DictionaryDto searchAll(String keyword) {
        return null;
    }

    @Override
    public Page<SentenceDto> searchSentence(String keyword, Pageable pageable) {
        Page<Sentence> sentencePage = sentenceService.search(keyword, pageable);
        List<Sentence> sentencePageContent = sentencePage.getContent();
        sentencePage.getTotalElements()
        return ;
    }

    @Override
    public Page<VocabularyDto> searchVocabulary(String keyword, Pageable pageable) {
        return null;
    }

    @Override
    public Page<VerbPhrase> searchVerbPhrase(String keyword, Pageable pageable) {
        return null;
    }


}
