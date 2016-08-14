package com.pce.service.mapper;

import com.pce.domain.Role;
import com.pce.domain.dto.DomainObjectDTO;
import com.pce.domain.dto.RoleDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Leonardo Tarjadi on 5/03/2016.
 */
@Component
public class RoleMapper extends AbstractEntityToDTOMapper<Role, DomainObjectDTO> implements EntityToDTOMapper {


  @Autowired
  private ModelMapper modelMapper;

  @Override
  public DomainObjectDTO mapEntityIntoDto(Role roleEntity) {
    return modelMapper.map(roleEntity, RoleDto.class);
  }

  @Override
  public Role mapDtoIntoEntity(DomainObjectDTO roleDto) {
    return modelMapper.map(roleDto, Role.class);
  }


}
