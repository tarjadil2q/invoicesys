package com.pce.service.mapper;

import com.pce.domain.PukGroup;
import com.pce.domain.dto.DomainObjectDTO;
import com.pce.domain.dto.PukGroupDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Leonardo Tarjadi on 19/08/2016.
 */
@Component
public class PukGroupMapper extends AbstractEntityToDTOMapper<PukGroup, DomainObjectDTO> implements EntityToDTOMapper {

  @Autowired
  private ModelMapper modelMapper;

  @Override
  public DomainObjectDTO mapEntityIntoDto(PukGroup pukGroup) {
    return modelMapper.map(pukGroup, PukGroupDto.class);
  }

  @Override
  public PukGroup mapDtoIntoEntity(DomainObjectDTO domainObjectDTO) {
    return modelMapper.map(domainObjectDTO, PukGroup.class);
  }
}
