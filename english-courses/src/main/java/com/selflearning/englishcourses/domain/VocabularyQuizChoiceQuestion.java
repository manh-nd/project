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
@ToString(exclude = "vocabularyQuizChoiceAnswers")
@EqualsAndHashCode(exclude = "vocabularyQuizChoiceAnswers")
@Entity
@Table(name = "vocabulary_quiz_choice_questions")
@Document(indexName = "vocabulary_quiz_choice_questions", shards = 2)
public class VocabularyQuizChoiceQuestion {

    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    @Column(name = "VOCABULARY_QUIZ_CHOICE_QUESTION_ID", length = 16)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "VOCABULARY_QUIZ_CHOICE_ID", nullable = false)
    private VocabularyQuizChoice vocabularyQuizChoice;

    @Column(name = "VOCABULARY_QUIZ_CHOICE_QUESTION", nullable = false)
    private String question;

    @Column(name = "QUESTION_TYPE", nullable = false)
    private QuestionType questionType;

    @OneToMany(mappedBy = "vocabularyQuizChoiceQuestion")
    private List<VocabularyQuizChoiceAnswer> vocabularyQuizChoiceAnswers;

}
