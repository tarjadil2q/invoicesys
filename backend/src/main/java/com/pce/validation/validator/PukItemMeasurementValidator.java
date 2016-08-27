package com.pce.validation.validator;

import com.pce.domain.PukItem;
import com.pce.domain.dto.PukItemDto;
import com.pce.service.PukItemMeasurementService;
import com.pce.validation.PukItemMeasureAssociation;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.Optional;

/**
 * Created by Leonardo Tarjadi on 27/08/2016.
 */
public class PukItemMeasurementValidator implements ConstraintValidator<PukItemMeasureAssociation, List<PukItemDto>> {

  @Autowired
  private PukItemMeasurementService pukItemMeasurementService;


  @Override
  public void initialize(PukItemMeasureAssociation constraintAnnotation) {
  }

  @Override
  public boolean isValid(List<PukItemDto> pukItems, ConstraintValidatorContext context) {
    for (PukItemDto pukItemDto : pukItems) {
      Optional<com.pce.domain.PukItemMeasurement> pukItemMeasurementById =
              pukItemMeasurementService.getPukItemMeasurementById(pukItemDto.getPukItemMeasurement().getPukItemMeasurementId());
      if (!pukItemMeasurementById.isPresent()) {
        return false;
      }
    }
    return true;
  }
}
