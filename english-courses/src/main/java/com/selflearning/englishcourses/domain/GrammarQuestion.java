package com.selflearning.englishcourses.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@ToString(exclude = {"answers"})
@EqualsAndHashCode(exclude = {"answers"})
@Entity
@Table(name = "grammar_questions")
@Document(indexName="grammar_questions", shards = 2)
public class GrammarQuestion {

    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    @Column(name = "GRAMMAR_QUESTION_ID", length = 16)
    private UUID id;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne
    @JoinColumn(name = "GRAMMAR_LESSON_ID", nullable = false)
    private GrammarLesson grammarLesson;

    @Column(name = "GRAMMAR_QUESTION_TEXT", nullable = false)
    private String question;

    @Column(name = "GRAMMAR_QUESTION_MEANING", nullable = false)
    private String meaning;

    @Column(name = "GRAMMAR_QUESTION_SUGGESTION")
    private String suggestion;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    private List<GrammarAnswer> answers;

    @Column(name="CREATED_TIME", insertable = false, updatable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private Date createdTime;

    @Column(name="UPDATED_TIME", insertable = false, updatable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private Date updatedTime;

}
