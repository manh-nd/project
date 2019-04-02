package com.selflearning.englishcourses.service;

import com.selflearning.englishcourses.domain.VerbPhrase;
import com.selflearning.englishcourses.domain.VerbPhraseDetail;
import com.selflearning.englishcourses.repository.elasticsearch.VerbPhraseElasticsearchRepository;
import com.selflearning.englishcourses.repository.jpa.VerbPhraseJpaRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class VerbPhraseServiceTest {

    @Autowired
    private VerbPhraseJpaRepository verbPhraseJpaRepository;

    @Autowired
    private VerbPhraseElasticsearchRepository verbPhraseElasticsearchRepository;

    @Test
    public void testSaveVerPhrase(){
        List<VerbPhrase> verbPhraseJpaRepositoryAll = verbPhraseJpaRepository.findAll();
        verbPhraseElasticsearchRepository.saveAll(verbPhraseJpaRepositoryAll);
    }

}
