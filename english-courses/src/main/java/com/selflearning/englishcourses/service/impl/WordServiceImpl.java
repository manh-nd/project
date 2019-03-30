package com.selflearning.englishcourses.service.impl;

import com.selflearning.englishcourses.domain.Word;
import com.selflearning.englishcourses.repository.jpa.WordJpaRepository;
import com.selflearning.englishcourses.service.WordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class WordServiceImpl implements WordService {

    private WordJpaRepository wordJpaRepository;

    @Autowired
    public void setWordJpaRepository(WordJpaRepository wordJpaRepository) {
        this.wordJpaRepository = wordJpaRepository;
    }

    @Override
    public Word get(String id) {
        return null;
    }

    @Override
    public Word get(UUID id) {
        return null;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void save(Word obj) {
        wordJpaRepository.save(obj);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void saveAll(Iterable<Word> iterable) {
        wordJpaRepository.saveAll(iterable);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void delete(Word obj) {
        wordJpaRepository.delete(obj);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void deleteAll(Iterable<Word> iterable) {
        wordJpaRepository.deleteAll(iterable);
    }

    @Override
    public Page<Word> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public Word convertDtoToEntity(UUID uuid) {
        return null;
    }

    @Override
    public List<Word> convertDtoToEntity(List<UUID> uuids) {
        return null;
    }

    @Override
    public UUID convertEntityToDto(Word entity) {
        return null;
    }

    @Override
    public List<UUID> convertEntityToDto(List<Word> entityList) {
        return null;
    }

}
