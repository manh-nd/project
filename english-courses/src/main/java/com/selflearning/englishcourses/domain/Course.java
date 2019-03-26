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
@ToString(exclude = {"lessons", "userCourses"})
@EqualsAndHashCode(exclude = {"lessons", "userCourses"})
@Entity
@Table(name = "courses")
@Document(indexName = "courses", shards = 2)
public class Course {

    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    @Column(name = "COURSE_ID", length = 16)
    private UUID id;

    @Column(name = "COURSE_NAME", nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "course")
    private List<Lesson> lessons;

    @OneToMany(mappedBy = "course")
    private List<UserCourse> userCourses;

}
