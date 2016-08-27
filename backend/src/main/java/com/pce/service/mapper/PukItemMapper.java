package com.pce.service.mapper;

import com.pce.domain.PukItem;
import com.pce.domain.dto.DomainObjectDTO;
import com.pce.domain.dto.PukItemDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Leonardo Tarjadi on 21/08/2016.
 */
@Component
public class PukItemMapper extends AbstractEntityToDTOMapper<PukItem, DomainObjectDTO> implements EntityToDTOMapper {

  @Autowired
  private ModelMapper modelMapper;

  @Override
  public DomainObjectDTO mapEntityIntoDto(PukItem pukItem) {

    return modelMapper.map(pukItem, PukItemDto.class);
  }

  @Override
  public PukItem mapDtoIntoEntity(DomainObjectDTO domainObjectDTO) {
    return modelMapper.map(domainObjectDTO, PukItem.class);
  }
}
