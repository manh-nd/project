package com.selflearning.englishcourses.service.impl;

import com.selflearning.englishcourses.domain.VerbPhraseDetail;
import com.selflearning.englishcourses.service.VerbPhraseDetailService;
import com.selflearning.englishcourses.service.dto.VerbPhraseDetailDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class VerbPhraseDetailServiceImpl implements VerbPhraseDetailService {

    private ModelMapper modelMapper;

    @Autowired
    public void setModelMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public VerbPhraseDetail convertDtoToEntity(VerbPhraseDetailDto verbPhraseDetailDto) {
        VerbPhraseDetail verbPhraseDetail = modelMapper.map(verbPhraseDetailDto, VerbPhraseDetail.class);
        UUID id = verbPhraseDetail.getId();
        if(Objects.nonNull(id)){
            verbPhraseDetail.setId(id);
        }
        return verbPhraseDetail;
    }

    @Override
    public List<VerbPhraseDetail> convertDtoToEntity(List<VerbPhraseDetailDto> verbPhraseDetailDtos) {
        return verbPhraseDetailDtos.stream().map(verbPhraseDetailDto -> convertDtoToEntity(verbPhraseDetailDto)).collect(Collectors.toList());
    }

    @Override
    public VerbPhraseDetailDto convertEntityToDto(VerbPhraseDetail verbPhraseDetail) {
        VerbPhraseDetailDto verbPhraseDetailDto = modelMapper.map(verbPhraseDetail, VerbPhraseDetailDto.class);
        String id = verbPhraseDetailDto.getId();
        if(Objects.nonNull(id)){
            verbPhraseDetailDto.setId(id.toString());
        }
        return verbPhraseDetailDto;
    }

    @Override
    public List<VerbPhraseDetailDto> convertEntityToDto(List<VerbPhraseDetail> verbPhraseDetails) {
        return verbPhraseDetails.stream().map(verbPhraseDetail -> convertEntityToDto(verbPhraseDetail)).collect(Collectors.toList());
    }
}
