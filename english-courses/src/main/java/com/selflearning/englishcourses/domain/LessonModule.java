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
@ToString(exclude = {"vocabularyLessons", "lessonModuleMarks"})
@EqualsAndHashCode(exclude = {"vocabularyLessons", "lessonModuleMarks"})
@Entity
@Table(name = "lesson_modules")
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

    @OneToMany(mappedBy = "lessonModule")
    private List<VocabularyLesson> vocabularyLessons;

    @OneToMany(mappedBy = "lessonModule")
    private List<LessonModuleMark> lessonModuleMarks;

}
