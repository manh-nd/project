package com.selflearning.englishcourses.repository.elasticsearch;

import com.selflearning.englishcourses.domain.Sentence;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.UUID;

public interface SentenceElasticsearchRepository extends ElasticsearchRepository<Sentence, UUID> {

    Page<Sentence> findByText(String text, Pageable pageable);
}
