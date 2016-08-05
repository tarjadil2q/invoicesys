package com.pce.service.mapper;

import com.pce.domain.dto.DomainObjectDTO;
import com.pce.domain.dto.RoleDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Iterator;
import java.util.List;

/**
 * Created by Leonardo Tarjadi on 5/03/2016.
 */
public interface EntityToDTOMapper <T> {

    DomainObjectDTO mapEntityIntoDTO(T entity);

    List<DomainObjectDTO> mapEntitiesIntoDTO(Iterable<T> entities);

    Page<DomainObjectDTO> mapEntityPageIntoDTOPage(Pageable pageRequest, Page<T> pageEntity);

}
