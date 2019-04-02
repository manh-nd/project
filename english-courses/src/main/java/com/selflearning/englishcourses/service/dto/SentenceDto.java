package com.selflearning.englishcourses.service.dto;


import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class SentenceDto {

    private String id;
    private String text;
    private String ipa;
    private String meaning;
    private String audioPath;
    private Date createdTime;
    private Date updatedTime;

}
