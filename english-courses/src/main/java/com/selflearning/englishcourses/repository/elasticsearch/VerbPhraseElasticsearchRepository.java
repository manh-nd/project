package com.selflearning.englishcourses.repository.elasticsearch;

import com.selflearning.englishcourses.domain.VerbPhrase;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.UUID;

public interface VerbPhraseElasticsearchRepository extends ElasticsearchRepository<VerbPhrase, UUID> {
}
