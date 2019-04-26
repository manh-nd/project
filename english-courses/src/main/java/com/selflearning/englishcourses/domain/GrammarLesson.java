package com.selflearning.englishcourses.domain;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@ToString(exclude = "questions")
@EqualsAndHashCode(exclude = "questions")
@Entity
@Table(name = "grammar_lessons")
@Document(indexName="grammar_lessons", shards = 2)
public class GrammarLesson {

    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    @Column(name = "GRAMMAR_LESSON_ID", length = 16)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "LESSON_MODULE_ID", nullable = false)
    private LessonModule lessonModule;

    @Column(name = "GRAMMAR_TITLE", nullable = false, unique = true)
    private String title;

    @Column(name = "GRAMMAR_CONTENT", columnDefinition = "LONGTEXT", nullable = false)
    private String content;

    @OneToMany(mappedBy = "grammarLesson", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GrammarQuestion> questions;

    @Column(name="CREATED_TIME", insertable = false, updatable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private Date createdTime;

    @Column(name="UPDATED_TIME", insertable = false, updatable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private Date updatedTime;

}
