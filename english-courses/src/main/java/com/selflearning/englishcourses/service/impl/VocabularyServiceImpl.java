package com.selflearning.englishcourses.service.impl;

import com.selflearning.englishcourses.domain.Vocabulary;
import com.selflearning.englishcourses.domain.Word;
import com.selflearning.englishcourses.domain.WordClass;
import com.selflearning.englishcourses.repository.elasticsearch.VocabularyElasticsearchRepository;
import com.selflearning.englishcourses.repository.elasticsearch.WordClassElasticsearchRepository;
import com.selflearning.englishcourses.repository.elasticsearch.WordElasticsearchRepository;
import com.selflearning.englishcourses.repository.jpa.VocabularyJpaRepository;
import com.selflearning.englishcourses.repository.jpa.WordClassJpaRepository;
import com.selflearning.englishcourses.repository.jpa.WordJpaRepository;
import com.selflearning.englishcourses.service.VocabularyService;
import com.selflearning.englishcourses.service.dto.VocabularyDto;
import org.elasticsearch.index.query.QueryBuilders;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.elasticsearch.index.query.QueryBuilders.*;

@Service
public class VocabularyServiceImpl implements VocabularyService {

    private ModelMapper modelMapper;
    private WordJpaRepository wordJpaRepository;
    private WordElasticsearchRepository wordElasticsearchRepository;
    private WordClassJpaRepository wordClassJpaRepository;
    private WordClassElasticsearchRepository wordClassElasticsearchRepository;
    private VocabularyJpaRepository vocabularyJpaRepository;
    private VocabularyElasticsearchRepository vocabularyElasticsearchRepository;

    @Autowired
    public void setModelMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Autowired
    public void setWordJpaRepository(WordJpaRepository wordJpaRepository) {
        this.wordJpaRepository = wordJpaRepository;
    }

    @Autowired
    public void setWordElasticsearchRepository(WordElasticsearchRepository wordElasticsearchRepository) {
        this.wordElasticsearchRepository = wordElasticsearchRepository;
    }

    @Autowired
    public void setWordClassJpaRepository(WordClassJpaRepository wordClassJpaRepository) {
        this.wordClassJpaRepository = wordClassJpaRepository;
    }

    @Autowired
    public void setWordClassElasticsearchRepository(WordClassElasticsearchRepository wordClassElasticsearchRepository) {
        this.wordClassElasticsearchRepository = wordClassElasticsearchRepository;
    }

    @Autowired
    public void setVocabularyJpaRepository(VocabularyJpaRepository vocabularyJpaRepository) {
        this.vocabularyJpaRepository = vocabularyJpaRepository;
    }

    @Autowired
    public void setVocabularyElasticsearchRepository(VocabularyElasticsearchRepository vocabularyElasticsearchRepository) {
        this.vocabularyElasticsearchRepository = vocabularyElasticsearchRepository;
    }

    @Override
    public Vocabulary get(String id) {
        return vocabularyJpaRepository.findById(UUID.fromString(id)).orElse(null);
    }

    @Override
    public Vocabulary get(UUID id) {
        return vocabularyJpaRepository.findById(id).orElse(null);
    }

    @Override
    public void save(Vocabulary obj) {
        vocabularyJpaRepository.save(obj);
        vocabularyElasticsearchRepository.save(obj);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void saveAll(Iterable<Vocabulary> iterable) {
        Iterator<Vocabulary> iterator = iterable.iterator();
        while (iterator.hasNext()) {
            Vocabulary vocabulary = iterator.next();
            Word word = vocabulary.getWord();
            Word wordInDb = wordJpaRepository.findByTextAndIpa(word.getText(), word.getIpa());
            if (Objects.nonNull(wordInDb)) {
                vocabulary.setWord(wordInDb);
            }
            WordClass wordClass = vocabulary.getWordClass();
            WordClass wordClassInDb = wordClassJpaRepository.findByName(wordClass.getName());
            if (Objects.nonNull(wordClassInDb)) {
                vocabulary.setWordClass(wordClassInDb);
            }
            vocabularyJpaRepository.save(vocabulary);
        }
        vocabularyElasticsearchRepository.saveAll(iterable);
    }

    @Transactional
    @Override
    public void delete(Vocabulary obj) {
        vocabularyJpaRepository.delete(obj);
        vocabularyElasticsearchRepository.delete(obj);
    }

    @Override
    public void deleteAll(Iterable<Vocabulary> iterable) {
        throw new RuntimeException("Not support yet.");
    }

    @Override
    public Page<Vocabulary> findAll(Pageable pageable) {
        return vocabularyJpaRepository.findAll(pageable);
    }

    @Override
    public Vocabulary convertDtoToEntity(VocabularyDto vocabularyDto) {
        Vocabulary vocabulary = modelMapper.map(vocabularyDto, Vocabulary.class);
        String id = vocabularyDto.getId();
        if (id != null) {
            vocabulary.setId(UUID.fromString(id));
        }
        return vocabulary;
    }

    @Override
    public List<Vocabulary> convertDtoToEntity(List<VocabularyDto> vocabularyDtos) {
        return vocabularyDtos.stream().map(vocabularyDto -> convertDtoToEntity(vocabularyDto)).collect(Collectors.toList());
    }

    @Override
    public VocabularyDto convertEntityToDto(Vocabulary vocabulary) {
        VocabularyDto vocabularyDto = modelMapper.map(vocabulary, VocabularyDto.class);
        UUID id = vocabulary.getId();
        if (id != null) {
            vocabularyDto.setId(id.toString());
        }
        return vocabularyDto;
    }

    @Override
    public List<VocabularyDto> convertEntityToDto(List<Vocabulary> vocabularies) {
        return vocabularies.stream().map(vocabulary -> convertEntityToDto(vocabulary)).collect(Collectors.toList());
    }

    @Override
    public Page<VocabularyDto> convertEntityPageToDtoPage(Page<Vocabulary> page) {
        return new PageImpl<>(convertEntityToDto(page.getContent()), page.getPageable(), page.getTotalElements());
    }

    @Override
    public Page<Vocabulary> search(String value, Pageable pageable) {
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(multiMatchQuery(value, "word.text", "meaning"))
                .withPageable(pageable)
                .build();
        return vocabularyElasticsearchRepository.search(searchQuery);
    }

    @Override
    public long count() {
        return vocabularyJpaRepository.count();
    }
}
