package com.selflearning.englishcourses.service.impl;

import com.selflearning.englishcourses.domain.Sentence;
import com.selflearning.englishcourses.repository.elasticsearch.SentenceElasticsearchRepository;
import com.selflearning.englishcourses.repository.jpa.SentenceJpaRepository;
import com.selflearning.englishcourses.service.SentenceService;
import com.selflearning.englishcourses.service.dto.SentenceDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.elasticsearch.index.query.QueryBuilders.multiMatchQuery;

@Service
public class SentenceServiceImpl implements SentenceService {

    private ModelMapper modelMapper;
    private SentenceJpaRepository sentenceJpaRepository;
    private SentenceElasticsearchRepository sentenceElasticsearchRepository;

    @Autowired
    public void setModelMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Autowired
    public void setSentenceElasticsearchRepository(SentenceElasticsearchRepository sentenceElasticsearchRepository) {
        this.sentenceElasticsearchRepository = sentenceElasticsearchRepository;
    }

    @Autowired
    public void setSentenceJpaRepository(SentenceJpaRepository sentenceJpaRepository) {
        this.sentenceJpaRepository = sentenceJpaRepository;
    }

    @Override
    public Sentence get(String id) {
        return sentenceJpaRepository.findById(UUID.fromString(id)).orElse(null);
    }

    @Override
    public Sentence get(UUID id) {
        return sentenceJpaRepository.findById(id).orElse(null);
    }

    @Transactional
    @Override
    public void save(Sentence sentence) {
        sentenceJpaRepository.save(sentence);
        sentenceElasticsearchRepository.save(sentence);
    }

    @Transactional
    @Override
    public void saveAll(Iterable<Sentence> iterable) {
        sentenceJpaRepository.saveAll(iterable);
        sentenceElasticsearchRepository.saveAll(iterable);
    }

    @Transactional
    @Override
    public void delete(Sentence sentence) {
        sentenceJpaRepository.delete(sentence);
        sentenceElasticsearchRepository.delete(sentence);
    }

    @Transactional
    @Override
    public void deleteAll(Iterable<Sentence> iterable) {
        sentenceJpaRepository.deleteAll(iterable);
        sentenceElasticsearchRepository.deleteAll(iterable);
    }

    @Override
    public Page<Sentence> findAll(Pageable pageable) {
        return sentenceJpaRepository.findAll(pageable);
    }

    @Override
    public Sentence convertDtoToEntity(SentenceDto sentenceDto) {
        Sentence sentence = modelMapper.map(sentenceDto, Sentence.class);
        String id = sentenceDto.getId();
        if (id != null)
            sentence.setId(UUID.fromString(id));
        return sentence;
    }

    @Override
    public List<Sentence> convertDtoToEntity(List<SentenceDto> sentenceDtos) {
        return sentenceDtos.stream().map(sentenceDto -> convertDtoToEntity(sentenceDto)).collect(Collectors.toList());
    }

    @Override
    public SentenceDto convertEntityToDto(Sentence sentence) {
        SentenceDto sentenceDto = modelMapper.map(sentence, SentenceDto.class);
        UUID id = sentence.getId();
        if (id != null)
            sentenceDto.setId(id.toString());
        return sentenceDto;
    }

    @Override
    public List<SentenceDto> convertEntityToDto(List<Sentence> sentences) {
        return sentences.stream().map(sentence -> convertEntityToDto(sentence)).collect(Collectors.toList());
    }

    @Override
    public Page<SentenceDto> convertEntityPageToDtoPage(Page<Sentence> page) {
        return new PageImpl<>(convertEntityToDto(page.getContent()), page.getPageable(), page.getTotalElements());
    }

    @Override
    public Page<Sentence> search(String value, Pageable pageable) {
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(multiMatchQuery(value, "text", "meaning"))
                .withPageable(pageable)
                .build();
        return sentenceElasticsearchRepository.search(searchQuery);
    }
}
