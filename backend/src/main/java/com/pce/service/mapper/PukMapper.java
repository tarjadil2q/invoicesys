package com.pce.service.mapper;

import com.pce.domain.Puk;
import com.pce.domain.dto.DomainObjectDTO;
import com.pce.domain.dto.PukDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Leonardo Tarjadi on 16/08/2016.
 */
@Component
public class PukMapper extends AbstractEntityToDTOMapper<Puk, DomainObjectDTO> implements EntityToDTOMapper {

  @Autowired
  private ModelMapper modelMapper;


  @Override
  public DomainObjectDTO mapEntityIntoDto(Puk puk) {
    return modelMapper.map(puk, PukDto.class);
  }

  @Override
  public Puk mapDtoIntoEntity(DomainObjectDTO domainObjectDTO) {
    return modelMapper.map(domainObjectDTO, Puk.class);
  }
}
