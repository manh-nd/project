package com.selflearning.englishcourses.service.dto;

import com.selflearning.englishcourses.domain.VerbPhraseDetail;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
public class VerbPhraseDto {

    private String id;
    private String text;
    private String ipa;
    private List<VerbPhraseDetailDto> verbPhraseDetails;
    private Date createdTime;
    private Date updatedTime;

}
