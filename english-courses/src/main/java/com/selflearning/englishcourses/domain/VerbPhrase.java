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
@ToString
@EqualsAndHashCode
@Entity
@Table(name="verb_phrases", uniqueConstraints = @UniqueConstraint(columnNames = {"VERB_PHRASE_TEXT", "VERB_PHRASE_IPA"}))
@Document(indexName = "verb_phrases", shards = 2)
public class VerbPhrase {

    @Id
    @GenericGenerator(name="uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    @Column(name = "VERB_PHRASE_ID", length = 16)
    private UUID id;

    @Column(name="VERB_PHRASE_TEXT", unique = true, nullable = false)
    private String text;

    @Column(name="VERB_PHRASE_IPA")
    private String ipa;

    @OneToMany(mappedBy = "verbPhrase", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<VerbPhraseDetail> verbPhraseDetails;

    @Column(name="CREATED_TIME", updatable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private Date createdTime;

    @Column(name="UPDATED_TIME", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private Date updatedTime;

}
