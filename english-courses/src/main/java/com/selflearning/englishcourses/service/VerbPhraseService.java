package com.selflearning.englishcourses.service;

import com.selflearning.englishcourses.domain.VerbPhrase;
import com.selflearning.englishcourses.service.dto.VerbPhraseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface VerbPhraseService extends BaseCurdService<VerbPhrase, UUID>, ModelMapperService<VerbPhrase, VerbPhraseDto> {

    Page<VerbPhrase> search(String value, Pageable pageable);

}
