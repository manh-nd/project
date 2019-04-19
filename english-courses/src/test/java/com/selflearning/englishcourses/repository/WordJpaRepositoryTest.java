package com.selflearning.englishcourses.repository;

import com.selflearning.englishcourses.repository.elasticsearch.WordElasticsearchRepository;
import com.selflearning.englishcourses.repository.jpa.WordJpaRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WordJpaRepositoryTest {

    @Autowired
    private WordJpaRepository wordJpaRepository;

    @Autowired
    private WordElasticsearchRepository wordElasticsearchRepository;

    @Test
    public void testSaveWordIntoElasticsearch(){
        wordElasticsearchRepository.saveAll(wordJpaRepository.findAll());
    }

}
