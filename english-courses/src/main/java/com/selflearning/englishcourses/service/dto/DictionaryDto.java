package com.selflearning.englishcourses.service.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

@Getter
@Setter
public class DictionaryDto {

    private Page<VocabularyDto> vocabularyDtoPage;
    private Page<VerbPhraseDto> verbPhraseDtoPage;
    private Page<SentenceDto> sentenceDtoPage;

}
