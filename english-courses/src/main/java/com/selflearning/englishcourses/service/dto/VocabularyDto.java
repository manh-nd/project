package com.selflearning.englishcourses.service.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class VocabularyDto {

    private String id;
    private WordDto word;
    private WordClassDto wordClass;
    private String description;
    private String meaning;
    private String imagePath;
    private Date createdTime;
    private Date updatedTime;

}
