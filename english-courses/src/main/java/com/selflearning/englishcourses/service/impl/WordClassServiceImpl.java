package com.selflearning.englishcourses.service.impl;

import com.selflearning.englishcourses.domain.WordClass;
import com.selflearning.englishcourses.repository.elasticsearch.WordClassElasticsearchRepository;
import com.selflearning.englishcourses.repository.jpa.WordClassJpaRepository;
import com.selflearning.englishcourses.service.WordClassService;
import com.selflearning.englishcourses.service.dto.WordClassDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class WordClassServiceImpl implements WordClassService {

    private WordClassJpaRepository wordClassJpaRepository;

    private WordClassElasticsearchRepository wordClassElasticsearchRepository;

    @Autowired
    public void setWordClassJpaRepository(WordClassJpaRepository wordClassJpaRepository) {
        this.wordClassJpaRepository = wordClassJpaRepository;
    }

    @Autowired
    public void setWordClassElasticsearchRepository(WordClassElasticsearchRepository wordClassElasticsearchRepository) {
        this.wordClassElasticsearchRepository = wordClassElasticsearchRepository;
    }

    @Override
    public WordClass get(String id) {
        return null;
    }

    @Override
    public WordClass get(UUID id) {
        return wordClassJpaRepository.getOne(id);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void save(WordClass obj) {
        wordClassJpaRepository.save(obj);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void saveAll(Iterable<WordClass> iterable) {
        wordClassJpaRepository.saveAll(iterable);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void delete(WordClass obj) {
        wordClassJpaRepository.delete(obj);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void deleteAll(Iterable<WordClass> iterable) {
        wordClassJpaRepository.deleteAll(iterable);
    }

    @Override
    public Page<WordClass> findAll(Pageable pageable) {
        return wordClassElasticsearchRepository.findAll(pageable);
    }

    @Override
    public WordClass convertDtoToEntity(WordClassDto wordClassDto) {
        return null;
    }

    @Override
    public List<WordClass> convertDtoToEntity(List<WordClassDto> wordClassDtos) {
        return null;
    }

    @Override
    public WordClassDto convertEntityToDto(WordClass entity) {
        return null;
    }

    @Override
    public List<WordClassDto> convertEntityToDto(List<WordClass> entityList) {
        return null;
    }
}
