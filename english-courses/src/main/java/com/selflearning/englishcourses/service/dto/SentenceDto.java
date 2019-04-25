package com.selflearning.englishcourses.service.dto;


import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.Date;

@Getter
@Setter
@ToString
public class SentenceDto extends BaseDto {

    @NotBlank
    @Length(max = 255, message = "error.sentence.text.length")
    private String text;

    @Length(max = 255, message = "error.sentence.ipa.length")
    private String ipa;

    @NotBlank
    @NotEmpty(message = "error.sentence.meaning.empty")
    @Length(max = 255, message = "error.sentence.meaning.length")
    private String meaning;

    @Length(max = 255, message = "error.sentence.audioPath.length")
    private String audioPath;

}
