package com.selflearning.englishcourses.service.impl;

import com.selflearning.englishcourses.domain.VerbPhrase;
import com.selflearning.englishcourses.domain.VerbPhraseDetail;
import com.selflearning.englishcourses.repository.elasticsearch.VerbPhraseElasticsearchRepository;
import com.selflearning.englishcourses.repository.jpa.VerbPhraseJpaRepository;
import com.selflearning.englishcourses.service.VerbPhraseDetailService;
import com.selflearning.englishcourses.service.VerbPhraseService;
import com.selflearning.englishcourses.service.dto.VerbPhraseDetailDto;
import com.selflearning.englishcourses.service.dto.VerbPhraseDto;
import org.elasticsearch.index.query.QueryBuilders;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.transaction.NotSupportedException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class VerbPhraseServiceImpl implements VerbPhraseService {


    private VerbPhraseJpaRepository verbPhraseJpaRepository;

    private VerbPhraseElasticsearchRepository verbPhraseElasticsearchRepository;

    private ModelMapper modelMapper;

    private VerbPhraseDetailService verbPhraseDetailService;

    @Override
    public VerbPhrase get(String id) {
        return null;
    }

    @Override
    public VerbPhrase get(UUID id) {
        return null;
    }

    @Transactional
    @Override
    public void save(VerbPhrase verbPhrase) {
        verbPhraseJpaRepository.save(verbPhrase);
        verbPhraseElasticsearchRepository.save(verbPhrase);
    }

    @Transactional
    @Override
    public void saveAll(Iterable<VerbPhrase> iterable) {
        verbPhraseJpaRepository.saveAll(iterable);
    }

    @Transactional
    @Override
    public void delete(VerbPhrase verbPhrase) {
        verbPhraseJpaRepository.delete(verbPhrase);
        verbPhraseElasticsearchRepository.delete(verbPhrase);
    }

    @Transactional
    @Override
    public void deleteAll(Iterable<VerbPhrase> iterable) {
        throw new RuntimeException("Not support this method");
    }

    @Override
    public Page<VerbPhrase> findAll(Pageable pageable) {
        return verbPhraseElasticsearchRepository.findAll(pageable);
    }

    @Override
    public VerbPhrase convertDtoToEntity(VerbPhraseDto verbPhraseDto) {
        VerbPhrase verbPhrase = modelMapper.map(verbPhraseDto, VerbPhrase.class);
        String id = verbPhraseDto.getId();
        List<VerbPhraseDetailDto> phraseDtos = verbPhraseDto.getVerbPhraseDetails();
        if (Objects.nonNull(id)) {
            verbPhrase.setId(UUID.fromString(id));
        }
        if(Objects.nonNull(phraseDtos)){
            List<VerbPhraseDetail> verbPhraseDetails = verbPhraseDetailService.convertDtoToEntity(phraseDtos);
            verbPhraseDetails.forEach(verbPhraseDetail->{
                verbPhraseDetail.setVerbPhrase(verbPhrase);
            });
            verbPhrase.setVerbPhraseDetails(verbPhraseDetails);
        }
        return verbPhrase;
    }

    @Override
    public List<VerbPhrase> convertDtoToEntity(List<VerbPhraseDto> verbPhraseDtos) {
        return verbPhraseDtos.stream().map(verbPhraseDto -> convertDtoToEntity(verbPhraseDto)).collect(Collectors.toList());
    }

    @Override
    public VerbPhraseDto convertEntityToDto(VerbPhrase verbPhrase) {
        VerbPhraseDto phraseDto = modelMapper.map(verbPhrase, VerbPhraseDto.class);
        UUID id = verbPhrase.getId();
        if(Objects.nonNull(id)){
            phraseDto.setId(id.toString());
        }
        return phraseDto;
    }

    @Override
    public List<VerbPhraseDto> convertEntityToDto(List<VerbPhrase> verbPhrases) {
        return verbPhrases.stream().map(verbPhrase-> convertEntityToDto(verbPhrase)).collect(Collectors.toList());
    }

    @Override
    public Page<VerbPhrase> search(String value, Pageable pageable) {
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.multiMatchQuery(value,
                        "text", "verbPhraseDetails.meaning", "verbPhraseDetails.description"))
                .withPageable(pageable)
                .build();
        return verbPhraseElasticsearchRepository.search(searchQuery);
    }


    @Autowired
    public void setVerbPhraseJpaRepository(VerbPhraseJpaRepository verbPhraseJpaRepository) {
        this.verbPhraseJpaRepository = verbPhraseJpaRepository;
    }

    @Autowired
    public void setVerbPhraseElasticsearchRepository(VerbPhraseElasticsearchRepository verbPhraseElasticsearchRepository) {
        this.verbPhraseElasticsearchRepository = verbPhraseElasticsearchRepository;
    }

    @Autowired
    public void setModelMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Autowired
    public void setVerbPhraseDetailService(VerbPhraseDetailService verbPhraseDetailService) {
        this.verbPhraseDetailService = verbPhraseDetailService;
    }
}
