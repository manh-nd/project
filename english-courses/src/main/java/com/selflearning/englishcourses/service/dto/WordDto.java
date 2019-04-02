package com.selflearning.englishcourses.service.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class WordDto {

    private String id;
    private String text;
    private String ipa;
    private String normalAudioPath;
    private String specialAudioPath;
    private Date createdTime;
    private Date updatedTime;

}
