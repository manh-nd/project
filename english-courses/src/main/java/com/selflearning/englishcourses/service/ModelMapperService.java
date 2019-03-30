package com.selflearning.englishcourses.service;

import java.util.List;

public interface ModelMapperService<E, Dto> {

    E convertDtoToEntity(Dto dto);

    List<E> convertDtoToEntity(List<Dto> dtoList);

    Dto convertEntityToDto(E entity);

    List<Dto> convertEntityToDto(List<E> entityList);

}
