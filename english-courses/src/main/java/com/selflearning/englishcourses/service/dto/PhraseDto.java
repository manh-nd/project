package com.selflearning.englishcourses.service.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class PhraseDto {

    private String id;
    private String text;
    private String ipa;
    private List<PhraseDetailDto> phraseDetails;
    private Date createdTime;
    private Date updatedTime;

}
