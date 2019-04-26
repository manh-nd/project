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
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "grammar_answers")
@Document(indexName="grammar_answers", shards = 2)
public class GrammarAnswer {

    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    @Column(name = "GRAMMAR_ANSWER_ID", length = 16)
    private UUID id;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne
    @JoinColumn(name = "GRAMMAR_QUESTION_ID", nullable = false)
    private GrammarQuestion question;

    @Column(name="GRAMMAR_ANSWER_TEXT", nullable = false)
    private String answer;

    @Column(name="GRAMMAR_ANSWER_MEANING")
    private String meaning;

    @Column(name="GRAMMAR_ANSWER_EXPLAIN")
    private String explain;

    @Column(name="GRAMMAR_RIGHT_ANSWER", nullable = false)
    private Boolean rightAnswer;

    @Column(name="CREATED_TIME", insertable = false, updatable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private Date createdTime;

    @Column(name="UPDATED_TIME", insertable = false, updatable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private Date updatedTime;

}
