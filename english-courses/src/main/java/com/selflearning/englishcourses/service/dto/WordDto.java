package com.selflearning.englishcourses.service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WordDto {

    private String id;
    private String text;
    private String ipa;
    private String normalAudioPath;
    private String specialAudioPath;

}
