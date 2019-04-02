package com.selflearning.englishcourses.service.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class VerbPhraseDetailDto {

    private String id;
    private VerbPhraseDto verbPhrase;
    private String meaning;
    private String description;
    private String synonyms;
    private String note;
    private Date createdTime;
    private Date updatedTime;

}
