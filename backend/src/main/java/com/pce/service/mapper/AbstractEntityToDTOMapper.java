package com.pce.service.mapper;

import com.google.common.base.Preconditions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leonardo Tarjadi on 14/08/2016.
 */
public abstract class AbstractEntityToDTOMapper<T, D> {

  public abstract D mapEntityIntoDto(T t);

  public abstract T mapDtoIntoEntity(D d);

  public List<D> mapEntitiesIntoDTO(Iterable<T> entities) {
    Preconditions.checkArgument(entities != null, "User entities cannot be null");
    List<D> userObjectDtos = new ArrayList<>();
    entities.forEach(t -> userObjectDtos.add(mapEntityIntoDto(t)));
    return userObjectDtos;
  }


  public Page<D> mapEntityPageIntoDTOPage(Pageable pageRequest, Page<T> pageEntity) {
    List<D> roleObjectDTOs = mapEntitiesIntoDTO(pageEntity.getContent());
    return new PageImpl<>(roleObjectDTOs, pageRequest, pageEntity.getTotalElements());
  }
}
