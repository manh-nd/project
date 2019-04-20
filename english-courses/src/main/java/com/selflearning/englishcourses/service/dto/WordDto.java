package com.selflearning.englishcourses.service.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class WordDto extends BaseDto {

    private String text;
    private String ipa;
    private String normalAudioPath;
    private String specialAudioPath;

}
