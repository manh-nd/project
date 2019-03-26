package com.selflearning.englishcourses.service;

import java.util.List;

public interface ModelMapperService<E, Dto> {

    E convertDtoToEntity(Dto dto);

    default List<E> convertDtoToEntity(List<Dto> dtoList){return null;}

    Dto convertEntityToDto(E entity);

    default List<Dto> convertEntityToDto(List<E> entityList){ return null;}

}
