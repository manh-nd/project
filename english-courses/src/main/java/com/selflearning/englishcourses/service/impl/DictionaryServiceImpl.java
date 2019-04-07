package com.selflearning.englishcourses.service.impl;

import com.selflearning.englishcourses.service.DictionaryService;
import com.selflearning.englishcourses.service.SentenceService;
import com.selflearning.englishcourses.service.VerbPhraseService;
import com.selflearning.englishcourses.service.VocabularyService;
import com.selflearning.englishcourses.service.dto.DictionaryDto;
import com.selflearning.englishcourses.service.dto.SentenceDto;
import com.selflearning.englishcourses.service.dto.VerbPhraseDto;
import com.selflearning.englishcourses.service.dto.VocabularyDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class DictionaryServiceImpl implements DictionaryService {

    private SentenceService sentenceService;

    private VerbPhraseService verbPhraseService;

    private VocabularyService vocabularyService;

    @Autowired
    public void setSentenceService(SentenceService sentenceService) {
        this.sentenceService = sentenceService;
    }

    @Autowired
    public void setVocabularyService(VocabularyService vocabularyService) {
        this.vocabularyService = vocabularyService;
    }

    @Autowired
    public void setVerbPhraseService(VerbPhraseService verbPhraseService) {
        this.verbPhraseService = verbPhraseService;
    }

    @Override
    public DictionaryDto searchAll(String keyword) {
        return null;
    }

    @Override
    public Page<SentenceDto> searchSentence(String keyword, Pageable pageable) {
        return sentenceService.convertEntityPageToDtoPage(sentenceService.search(keyword, pageable));
    }

    @Override
    public Page<VocabularyDto> searchVocabulary(String keyword, Pageable pageable) {
        return vocabularyService.convertEntityPageToDtoPage(vocabularyService.search(keyword, pageable));
    }

    @Override
    public Page<VerbPhraseDto> searchVerbPhrase(String keyword, Pageable pageable) {
        return verbPhraseService.convertEntityPageToDtoPage(verbPhraseService.search(keyword, pageable));
    }


}
