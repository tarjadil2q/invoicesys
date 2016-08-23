package com.pce.service.mapper;

import com.pce.domain.PukItemMeasurement;
import com.pce.domain.dto.DomainObjectDTO;
import com.pce.domain.dto.PukItemMeasurementDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Leonardo Tarjadi on 23/08/2016.
 */
public class PukItemMeasurementMapper extends AbstractEntityToDTOMapper<PukItemMeasurement, DomainObjectDTO> implements EntityToDTOMapper {

  @Autowired
  private ModelMapper modelMapper;

  @Override
  public DomainObjectDTO mapEntityIntoDto(PukItemMeasurement pukItemMeasurement) {
    return modelMapper.map(pukItemMeasurement, PukItemMeasurementDto.class);
  }

  @Override
  public PukItemMeasurement mapDtoIntoEntity(DomainObjectDTO domainObjectDTO) {
    return modelMapper.map(domainObjectDTO, PukItemMeasurement.class);
  }
}
