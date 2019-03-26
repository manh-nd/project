package com.selflearning.englishcourses.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

/**
 * @author manhnd
 * @version 1.0
 */
@Getter
@Setter
@ToString(exclude = "vocabularies")
@EqualsAndHashCode(exclude = "vocabularies")
@Entity
@Table(name="word_classes")
@Document(indexName = "word_classes", shards = 2)
public class WordClass {

    @Id
    @GenericGenerator(name="uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    @Column(name = "WORD_CLASS_ID", length = 16)
    private UUID id;

    @Column(name="WORD_CLASS_NAME", length = 80, nullable = false, unique = true)
    private String name;

    @Column(name="WORD_CLASS_MEANING", length = 80, nullable = false)
    private String meaning;

    @Column(name="WORD_CLASS_DESCRIPTION", length = 1000)
    private String description;

    @OneToMany(mappedBy = "wordClass", cascade = CascadeType.ALL)
    private List<Vocabulary> vocabularies;
}
