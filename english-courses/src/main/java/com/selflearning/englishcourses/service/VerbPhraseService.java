package com.selflearning.englishcourses.service;

import com.selflearning.englishcourses.domain.VerbPhrase;
import com.selflearning.englishcourses.service.dto.VerbPhraseDto;

import java.util.UUID;

public interface VerbPhraseService extends BaseCurdService<VerbPhrase, UUID>, ModelMapperService<VerbPhrase, VerbPhraseDto> {

}
