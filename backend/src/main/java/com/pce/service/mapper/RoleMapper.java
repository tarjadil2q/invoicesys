package com.pce.service.mapper;

import com.google.common.base.Preconditions;
import com.pce.domain.Role;
import com.pce.domain.User;
import com.pce.domain.dto.DomainObjectDTO;
import com.pce.domain.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leonardo Tarjadi on 5/03/2016.
 */
@Service
public  class RoleMapper implements EntityToDTOMapper<Role> {

    @Override
    public DomainObjectDTO mapEntityIntoDTO(Role entity) {
        return null;
    }

    @Override
    public List<DomainObjectDTO> mapEntitiesIntoDTO(Iterable<Role> entities) {

        Preconditions.checkArgument(entities != null,  "Role entities cannot be null");
        List<DomainObjectDTO> roleObjectDTOs = new ArrayList<>();
        entities.forEach(role -> roleObjectDTOs.add(mapEntityIntoDTO(role)));
        return roleObjectDTOs;

    }

    @Override
    public Page<DomainObjectDTO> mapEntityPageIntoDTOPage(Pageable pageRequest, Page<Role> pageEntity) {
        List<DomainObjectDTO> roleObjectDTOs = mapEntitiesIntoDTO(pageEntity.getContent());
        return new PageImpl<>(roleObjectDTOs, pageRequest, pageEntity.getTotalElements());
    }

    //TODO
    @Override
    public User mapDtoIntoEntity(UserDto userDto) {
        return null;
    }
}
