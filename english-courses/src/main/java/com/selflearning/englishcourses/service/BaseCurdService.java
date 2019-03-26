package com.selflearning.englishcourses.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;
import java.util.UUID;

public interface BaseCurdService<T, ID extends Serializable> {

    T get(String id);

    T get(UUID id);

    void save(T obj);

    void saveAll(Iterable<T> iterable);

    void delete(T obj);

    void deleteAll(Iterable<T> iterable);

    Page<T> findAll(Pageable pageable);

}
