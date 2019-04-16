package com.selflearning.englishcourses.domain;

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

/**
 * @author manhnd
 * @version 1.0
 */
@Getter
@Setter
@ToString(exclude = {"vocabularyLessons", "lessonModuleMarks", "phraseLessons", "grammarLessons"})
@EqualsAndHashCode(exclude = {"vocabularyLessons", "lessonModuleMarks", "phraseLessons", "grammarLessons"})
@Entity
@Table(name = "lesson_modules", uniqueConstraints = @UniqueConstraint(columnNames = {"LESSON_ID", "MODULE_ID"}))
@Document(indexName = "lesson_modules", shards = 2)
public class LessonModule {

    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    @Column(name = "LESSON_MODULE_ID", length = 16)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "LESSON_ID", nullable = false)
    private Lesson lesson;

    @ManyToOne
    @JoinColumn(name = "MODULE_ID", nullable = false)
    private Module module;

    @OneToMany(mappedBy = "lessonModule", cascade = CascadeType.ALL)
    private List<VocabularyLesson> vocabularyLessons;

    @OneToMany(mappedBy = "lessonModule", cascade = CascadeType.ALL)
    private List<LessonModuleMark> lessonModuleMarks;

    @OneToMany(mappedBy = "lessonModule", cascade = CascadeType.ALL)
    private List<PhraseLesson> phraseLessons;

    @OneToMany(mappedBy = "lessonModule", cascade = CascadeType.ALL)
    private List<GrammarLesson> grammarLessons;

    @Column(name="CREATED_TIME", insertable = false, updatable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private Date createdTime;

    @Column(name="UPDATED_TIME", insertable = false, updatable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private Date updatedTime;

}
